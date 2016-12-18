// author: a1pcm

package com.cc

import swing._
import swing.event._

import java.awt.Color
import java.awt.Dimension

class ColorButton(var ccolor: Color = Color.BLACK) extends Button {
  {
    val s = new Dimension(26, 26)
    
    minimumSize = s
    maximumSize = s
    preferredSize = s
  }
  
  val cchooser = new ColorChooser()
  
  override def paintComponent(g2: Graphics2D) {
    super.paintComponent(g2)
    
    g2 setColor ccolor
    g2 fillRect(0, 0, preferredSize.width, preferredSize.height)
  }
  
  reactions += {
    case ButtonClicked(_) => {
      ColorChooser.showDialog(cchooser, 
          "Choose Panel Divider Color", ccolor) match {
        case Some(c) => ccolor = c
        case None =>
      }
    }
  }
}