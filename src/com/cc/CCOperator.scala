// author: a1pcm

package com.cc

import java.awt.Color
import java.awt.Font

import java.awt.image.BufferedImage

import java.io.File

import javax.imageio.ImageIO

class CCOperator(dir: File, dheight: Int = 15, width: Int = 900,
                 dcolor: Color = Color.BLACK, debug: Boolean = false) {
  def iconcat(head: BufferedImage, tail: BufferedImage) = {
    val result = new BufferedImage(head getWidth, (head getHeight) +
      (tail getHeight), BufferedImage.TYPE_INT_ARGB)
    val g = result getGraphics

    g drawImage (head, 0, 0, null)
    g drawImage (tail, 0, head getHeight, null)
    g dispose

    result
  }

  def rstrToList(range: String): List[Int] =
    if (range contains "-") {
      val list = (range split "-") map { s => s toInt }
      List range (list(0), list(1))
    } else List(range toInt)

  def drawPanelLabel(image: BufferedImage, text: String,
                     x: Int, y: Int): BufferedImage = {
    val result = new BufferedImage(image getColorModel,
      image copyData (null),
      (image getColorModel) isAlphaPremultiplied, null)
    val g = result getGraphics

    g setColor (Color.RED)
    g setFont (new Font("Courier New", Font.BOLD, 18))
    g drawString (text, x, y)
    g dispose

    result
  }

  def go {
    val div: BufferedImage = new BufferedImage(width, dheight,
      BufferedImage.TYPE_INT_ARGB)

    val g = (div getGraphics)
    g setColor dcolor
    g fillRect (0, 0, width, dheight)
    g dispose

    var images = (dir listFiles) filter { x => x isDirectory } map { d =>
      (d getName, ((d listFiles) filter
        { f => (f isFile) && ((f getName) endsWith ".png") }) map
        { f => ImageIO read (f) })
    }

    if (debug)
      images = images map { p =>
        (p._1, (rstrToList(p._1) zip p._2) map
          { q => drawPanelLabel(q._2, q._1 toString, 15, 15) } toArray)
      }

    images map { p =>
      (new File((dir getCanonicalPath) + "/" + p._1 + ".png"),
        (p._2 flatMap { i => List(div, i) } tail) reduce iconcat)
    } foreach (p => ImageIO.write(p._2, "png", p._1))
  }
}