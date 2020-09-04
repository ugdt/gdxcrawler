package com.abysl.gdxcrawler.ecs.components

import com.abysl.gdxcrawler.physics.Body
import com.abysl.gdxcrawler.physics.RectBody
import com.artemis.Component

class BodyComponent(
    var body: Body = RectBody(1f, 1f)
) : Component()
