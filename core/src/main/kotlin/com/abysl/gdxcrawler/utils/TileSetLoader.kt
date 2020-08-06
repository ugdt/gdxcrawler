package com.abysl.gdxcrawler.utils

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.Texture.TextureFilter
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.ImageResolver
import com.badlogic.gdx.maps.ImageResolver.AssetManagerImageResolver
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.MapObjects
import com.badlogic.gdx.maps.MapProperties
import com.badlogic.gdx.maps.objects.EllipseMapObject
import com.badlogic.gdx.maps.objects.PolygonMapObject
import com.badlogic.gdx.maps.objects.PolylineMapObject
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.maps.tiled.TiledMapTileSet
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile
import com.badlogic.gdx.math.Polygon
import com.badlogic.gdx.math.Polyline
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.GdxRuntimeException
import com.badlogic.gdx.utils.XmlReader
import ktx.collections.GdxIntArray
import java.util.*

class TileSetLoader(resolver: FileHandleResolver) : AsynchronousAssetLoader<TiledMapTileSet, TileSetLoader.TileSetParameters>(resolver) {
    class TileSetParameters : AssetLoaderParameters<TiledMapTileSet>() {
        /** generate mipmaps?  */
        var generateMipMaps = false

        /** The TextureFilter to use for minification  */
        var textureMinFilter = TextureFilter.Nearest

        /** The TextureFilter to use for magnification  */
        var textureMagFilter = TextureFilter.Nearest
    }

    private var xml = XmlReader()
    private lateinit var tileSetElement: XmlReader.Element
    private var convertObjectToTileSpace = false
    private var flipY = true
    private var mapTileWidth = 0
    private var mapTileHeight = 0
    private lateinit var tileSet: TiledMapTileSet

    override fun loadAsync(manager: AssetManager, fileName: String, tsxFile: FileHandle, parameter: TileSetParameters?) {
        tileSet = loadTileSet(tileSetElement, tsxFile, AssetManagerImageResolver(manager))
    }

    override fun loadSync(manager: AssetManager, fileName: String, file: FileHandle, parameter: TileSetParameters?): TiledMapTileSet {
        return tileSet
    }

    override fun getDependencies(fileName: String, tsxFile: FileHandle, parameter: TileSetParameters?): Array<AssetDescriptor<*>> {
        this.tileSetElement = xml.parse(tsxFile)
        val textureParameter = TextureParameter()

        if (parameter != null) {
            textureParameter.genMipMaps = parameter.generateMipMaps
            textureParameter.minFilter = parameter.textureMinFilter
            textureParameter.magFilter = parameter.textureMagFilter
        }

        return getDependencyAssetDescriptors(tsxFile, textureParameter)
    }

    private fun getDependencyAssetDescriptors(tsxFile: FileHandle, textureParameter: TextureParameter): Array<AssetDescriptor<*>> {
        val descriptors = Array<AssetDescriptor<*>>()
        val fileHandles: Array<FileHandle> = getDependencyFileHandles(tsxFile)
        for (handle in fileHandles) {
            descriptors.add(AssetDescriptor(handle, Texture::class.java, textureParameter))
        }
        return descriptors
    }

    private fun getDependencyFileHandles(tsxFile: FileHandle): Array<FileHandle> {
        val fileHandles = Array<FileHandle>()

        val tileSet = tileSetElement

        val imageElement = tileSet.getChildByName("image")

        if (imageElement != null) {
            val imageSource = tileSet.getChildByName("image").getAttribute("source")
            val image = getRelativeFileHandle(tsxFile, imageSource)
            fileHandles.add(image)
        } else {
            for (tile in tileSet.getChildrenByName("tile")) {
                val imageSource = tile.getChildByName("image").getAttribute("source")
                val image = getRelativeFileHandle(tsxFile, imageSource)
                fileHandles.add(image)
            }
        }

        for (imageLayer in this.tileSetElement.getChildrenByName("imagelayer")) {
            val image = imageLayer.getChildByName("image")
            val source = image.getAttribute("source", null)
            if (source != null) {
                val handle = getRelativeFileHandle(tsxFile, source)
                fileHandles.add(handle)
            }
        }

        return fileHandles
    }

    private fun loadObject(tileSet: TiledMapTileSet, tile: TiledMapTile, element: XmlReader.Element) {
        loadObject(tileSet, tile.objects, element, tile.textureRegion.regionHeight.toFloat())
    }

    private fun loadObject(tileSet: TiledMapTileSet, objects: MapObjects, element: XmlReader.Element, heightInPixels: Float) {
        if (element.name == "object") {
            var mapObject: MapObject? = null

            val scaleX = if (convertObjectToTileSpace) 1.0f / mapTileWidth else 1.0f
            val scaleY = if (convertObjectToTileSpace) 1.0f / mapTileHeight else 1.0f

            val x = element.getFloatAttribute("x", 0f) * scaleX
            val y = (if (flipY) heightInPixels - element.getFloatAttribute("y", 0f) else element.getFloatAttribute("y", 0f)) * scaleY

            val width = element.getFloatAttribute("width", 0f) * scaleX
            val height = element.getFloatAttribute("height", 0f) * scaleY

            if (element.childCount > 0) {
                var child: XmlReader.Element

                when {
                    (element.getChildByName("polygon").also { child = it } != null ||
                            element.getChildByName("polyline").also { child = it } != null) -> {
                        val name = child.name.orEmpty()

                        val points = child.getAttribute("points").split(" ").toTypedArray()
                        val vertices = FloatArray(points.size * 2)

                        for (i in points.indices) {
                            val point = points[i].split(",").toTypedArray()
                            vertices[i * 2] = point[0].toFloat() * scaleX
                            vertices[i * 2 + 1] = point[1].toFloat() * scaleY * if (flipY) -1 else 1
                        }

                        when (name) {
                            "polygon" -> {
                                val polygon = Polygon(vertices)
                                polygon.setPosition(x, y)
                                mapObject = PolygonMapObject(polygon)
                            }
                            "polyline" -> {
                                val polyline = Polyline(vertices)
                                polyline.setPosition(x, y)
                                mapObject = PolylineMapObject(polyline)
                            }
                        }
                    }
                    element.getChildByName("ellipse") != null -> {
                        mapObject = EllipseMapObject(x, if (flipY) y - height else y, width, height)
                    }
                }
            }
            if (mapObject == null) {
                var gid: String

                if (element.getAttribute("gid", null).also { gid = it } != null) {
                    val id = gid.toLong().toInt()

                    val flipHorizontally = id and FLAG_FLIP_HORIZONTALLY != 0
                    val flipVertically = id and FLAG_FLIP_VERTICALLY != 0

                    val tile = tileSet.getTile(id and MASK_CLEAR.inv())
                    val tiledMapTileMapObject = TiledMapTileMapObject(tile, flipHorizontally, flipVertically)
                    val textureRegion = tiledMapTileMapObject.textureRegion

                    tiledMapTileMapObject.properties.put("gid", id)
                    tiledMapTileMapObject.x = x
                    tiledMapTileMapObject.y = if (flipY) y else y - height

                    val objectWidth = element.getFloatAttribute("width", textureRegion.regionWidth.toFloat())
                    val objectHeight = element.getFloatAttribute("height", textureRegion.regionHeight.toFloat())

                    tiledMapTileMapObject.scaleX = scaleX * (objectWidth / textureRegion.regionWidth)
                    tiledMapTileMapObject.scaleY = scaleY * (objectHeight / textureRegion.regionHeight)
                    tiledMapTileMapObject.rotation = element.getFloatAttribute("rotation", 0f)

                    mapObject = tiledMapTileMapObject
                } else {
                    mapObject = RectangleMapObject(x, if (flipY) y - height else y, width, height)
                }
            }
            mapObject.name = element.getAttribute("name", null)
            val rotation = element.getAttribute("rotation", null)

            if (rotation != null) {
                mapObject.properties.put("rotation", rotation.toFloat())
            }

            val type = element.getAttribute("type", null)
            if (type != null) {
                mapObject.properties.put("type", type)
            }

            val id = element.getIntAttribute("id", 0)
            if (id != 0) {
                mapObject.properties.put("id", id)
            }

            mapObject.properties.put("x", x)
            if (mapObject is TiledMapTileMapObject) {
                mapObject.getProperties().put("y", y)
            } else {
                mapObject.properties.put("y", if (flipY) y - height else y)
            }

            mapObject.properties.put("width", width)
            mapObject.properties.put("height", height)
            mapObject.isVisible = element.getIntAttribute("visible", 1) == 1

            val properties = element.getChildByName("properties")
            if (properties != null) {
                loadProperties(mapObject.properties, properties)
            }

            objects.add(mapObject)
        }
    }

    private fun loadProperties(properties: MapProperties, element: XmlReader.Element?) {
        if (element == null) return

        if (element.name == "properties") {
            for (property in element.getChildrenByName("property")) {
                val name = property.getAttribute("name", null)
                var value = property.getAttribute("value", null)
                val type = property.getAttribute("type", null)

                if (value == null) {
                    value = property.text
                }

                val castValue = castProperty(name, value, type)
                properties.put(name, castValue)
            }
        }
    }

    private fun castProperty(name: String, value: String?, type: String?): Any? {
        if (value == null) {
            return null
        }

        return when (type) {
            null -> {
                value
            }
            "int" -> {
                value.toInt()
            }
            "float" -> {
                value.toFloat()
            }
            "bool" -> {
                value.toBoolean()
            }
            "color" -> {
                // Tiled uses the format #AARRGGBB
                val opaqueColor = value.substring(3)
                val alpha = value.substring(1, 3)
                Color.valueOf(opaqueColor + alpha)
            }
            else -> {
                throw GdxRuntimeException("Wrong type given for property " + name + ", given : " + type
                        + ", supported : string, bool, int, float, color")
            }
        }
    }

    private fun loadTileSet(element: XmlReader.Element, tsxFile: FileHandle, imageResolver: ImageResolver): TiledMapTileSet {
        val tileSet = TiledMapTileSet()

        val firstGid = element.getIntAttribute("firstgid", 1)
        var imageSource = ""
        var imageWidth = 0
        var imageHeight = 0
        var image: FileHandle? = null


        val imageElement = element.getChildByName("image")

        if (imageElement != null) {
            imageSource = imageElement.getAttribute("source")
            imageWidth = imageElement.getIntAttribute("width", 0)
            imageHeight = imageElement.getIntAttribute("height", 0)
            image = getRelativeFileHandle(tsxFile, imageSource)
        }

        val name = element.get("name", null)
        val tileWidth = element.getIntAttribute("tilewidth", 0)
        val tileHeight = element.getIntAttribute("tileheight", 0)
        val spacing = element.getIntAttribute("spacing", 0)
        val margin = element.getIntAttribute("margin", 0)
        val offset = element.getChildByName("tileoffset")
        var offsetX = 0
        var offsetY = 0

        if (offset != null) {
            offsetX = offset.getIntAttribute("x", 0)
            offsetY = offset.getIntAttribute("y", 0)
        }

        // TileSet
        tileSet.name = name
        val tileSetProperties = tileSet.properties
        val properties = element.getChildByName("properties")
        properties?.let { loadProperties(tileSetProperties, it) }
        tileSetProperties.put("firstgid", firstGid)

        // Tiles
        val tileElements = element.getChildrenByName("tile")
        addStaticTiles(tsxFile, imageResolver, tileSet, tileElements, firstGid, tileWidth, tileHeight, spacing, margin, offsetX,
                offsetY, imageSource, imageWidth, imageHeight, image)
        val animatedTiles = Array<AnimatedTiledMapTile>()
        for (tileElement in tileElements) {
            val localTileId = tileElement.getIntAttribute("id", 0)
            var tile = tileSet.getTile(firstGid + localTileId)

            if (tile != null) {
                val animatedTile = createAnimatedTile(tileSet, tile, tileElement, firstGid)

                if (animatedTile != null) {
                    animatedTiles.add(animatedTile)
                    tile = animatedTile
                }

                addTileProperties(tile, tileElement)
                addTileObjectGroup(tile, tileElement)
            }
        }

        // replace original static tiles by animated tiles
        for (animatedTile in animatedTiles) {
            tileSet.putTile(animatedTile.id, animatedTile)
        }

        return tileSet
    }

    private fun addStaticTiles(tsxFile: FileHandle, imageResolver: ImageResolver, tileSet: TiledMapTileSet, tileElements: Array<XmlReader.Element>,
                               firstgid: Int, tilewidth: Int, tileheight: Int, spacing: Int, margin: Int, offsetX: Int, offsetY: Int,
                               imageSource: String?, imageWidth: Int, imageHeight: Int, image: FileHandle?) {
        var mImageSource = imageSource
        val props = tileSet.properties
        if (image != null) {
            // One image for the whole tileSet
            val texture = imageResolver.getImage(image.path())
            props.put("imagesource", mImageSource)
            props.put("imagewidth", imageWidth)
            props.put("imageheight", imageHeight)
            props.put("tilewidth", tilewidth)
            props.put("tileheight", tileheight)
            props.put("margin", margin)
            props.put("spacing", spacing)
            val stopWidth = texture.regionWidth - tilewidth
            val stopHeight = texture.regionHeight - tileheight
            var id = firstgid
            var y = margin
            while (y <= stopHeight) {
                var x = margin
                while (x <= stopWidth) {
                    val tileRegion = TextureRegion(texture, x, y, tilewidth, tileheight)
                    val tileId = id++
                    addStaticTiledMapTile(tileSet, tileRegion, tileId, offsetX.toFloat(), offsetY.toFloat())
                    x += tilewidth + spacing
                }
                y += tileheight + spacing
            }
        } else {
            // Every tile has its own image source
            for (tileElement in tileElements) {
                val imageElement = tileElement.getChildByName("image")
                if (imageElement != null) {
                    mImageSource = imageElement.getAttribute("source")
                    getRelativeFileHandle(tsxFile, mImageSource)
                }
                val texture = imageResolver.getImage(image?.path())
                val tileId = firstgid + tileElement.getIntAttribute("id")
                addStaticTiledMapTile(tileSet, texture, tileId, offsetX.toFloat(), offsetY.toFloat())
            }
        }
    }

    private fun addTileProperties(tile: TiledMapTile, tileElement: XmlReader.Element) {
        val terrain = tileElement.getAttribute("terrain", null)
        if (terrain != null) {
            tile.properties.put("terrain", terrain)
        }
        val probability = tileElement.getAttribute("probability", null)
        if (probability != null) {
            tile.properties.put("probability", probability)
        }
        val properties = tileElement.getChildByName("properties")
        if (properties != null) {
            loadProperties(tile.properties, properties)
        }
    }

    private fun addTileObjectGroup(tile: TiledMapTile, tileElement: XmlReader.Element) {
        val objectgroupElement = tileElement.getChildByName("objectgroup")

        if (objectgroupElement != null) {
            for (objectElement in objectgroupElement.getChildrenByName("object")) {
                loadObject(tileSet, tile, objectElement)
            }
        }
    }

    private fun createAnimatedTile(tileSet: TiledMapTileSet, tile: TiledMapTile, tileElement: XmlReader.Element,
                                   firstgid: Int): AnimatedTiledMapTile? {
        val animationElement = tileElement.getChildByName("animation")

        if (animationElement != null) {
            val staticTiles = Array<StaticTiledMapTile>()
            val intervals = GdxIntArray()
            for (frameElement in animationElement.getChildrenByName("frame")) {
                staticTiles.add(tileSet.getTile(firstgid + frameElement.getIntAttribute("tileid")) as StaticTiledMapTile)
                intervals.add(frameElement.getIntAttribute("duration"))
            }

            val animatedTile = AnimatedTiledMapTile(intervals, staticTiles)
            animatedTile.id = tile.id
            return animatedTile
        }

        return null
    }

    private fun addStaticTiledMapTile(tileSet: TiledMapTileSet, textureRegion: TextureRegion?, tileId: Int, offsetX: Float,
                                      offsetY: Float) {
        val tile: TiledMapTile = StaticTiledMapTile(textureRegion)
        tile.id = tileId
        tile.offsetX = offsetX
        tile.offsetY = if (flipY) -offsetY else offsetY
        tileSet.putTile(tileId, tile)
    }

    companion object {
        private const val FLAG_FLIP_HORIZONTALLY = -0x80000000
        private const val FLAG_FLIP_VERTICALLY = 0x40000000
        private const val MASK_CLEAR = -0x20000000

        private fun getRelativeFileHandle(file: FileHandle, path: String?): FileHandle {
            val tokenizer = StringTokenizer(path, "\\/")
            var result = file.parent()
            while (tokenizer.hasMoreElements()) {
                val token = tokenizer.nextToken()
                result = if (token == "..") result.parent() else {
                    result.child(token)
                }
            }
            return result
        }
    }
}