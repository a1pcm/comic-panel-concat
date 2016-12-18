// author: a1pcm

package com.cc

import swing._
import swing.event._

import java.io.File

object ComicConcatenator extends SimpleSwingApplication {
  var F_root = new File("./")
  val TF_rootdir = new TextField {
    columns = 15
    text = F_root getCanonicalPath
  }
  val TF_divheight = new TextField {
    columns = 3
    text = "15"

    listenTo(keys)
    reactions += {
      case e: KeyTyped =>
        if (!(e.char isDigit))
          e consume
    }
  }
  val TF_width = new TextField {
    columns = 4
    text = "900"

    listenTo(keys)
    reactions += {
      case e: KeyTyped =>
        if (!(e.char isDigit))
          e consume
    }
  }
  val B_color = new ColorButton()
  val CB_debug = new CheckBox("Debug Mode")

  def top = new MainFrame {
    contents = new GridBagPanel {
      def constraints(x: Int, y: Int,
                      gridwidth: Int = 1, gridheight: Int = 1,
                      weightx: Double = 0.0, weighty: Double = 0.0,
                      fill: GridBagPanel.Fill.Value = GridBagPanel.Fill.None): Constraints = {
        val c = new Constraints
        c.gridx = x
        c.gridy = y
        c.gridwidth = gridwidth
        c.gridheight = gridheight
        c.weightx = weightx
        c.weighty = weighty
        c.fill = fill
        c
      } /* this method taken from the Scala GridBagPanel example 
      at http://otfried.org/scala/index_42.html */

      add(new FlowPanel {
        contents += new Label("Root Directory: ")
        contents += TF_rootdir
      },
        constraints(0, 0, gridwidth = 2,
          fill = GridBagPanel.Fill.Horizontal))
      add(new Button("Select") {
        reactions += {
          case ButtonClicked(_) => {
            val chooser = new FileChooser(F_root) {
              title = "Choose the root directory..."
              fileSelectionMode = FileChooser.SelectionMode.DirectoriesOnly
            }
            val result = chooser.showOpenDialog(null)

            if (result == FileChooser.Result.Approve)
              TF_rootdir.text = chooser.selectedFile getCanonicalPath
          }
        }
      }, constraints(2, 0))
      add(new BoxPanel(Orientation.Vertical) {
        contents += new FlowPanel {
          contents += new Label("Divider Height")
          contents += TF_divheight
        }
        contents += new FlowPanel {
          contents += new Label("Comic Width")
          contents += TF_width
        }
      }, constraints(0, 1))
      add(new BoxPanel(Orientation.Vertical) {
        contents += new FlowPanel {
          contents += new Label("Divider Color")
          contents += B_color
        }
        contents += new FlowPanel {
          contents += CB_debug
        }
      }, constraints(1, 1))
      add(new Button("Go") {
        reactions += {
          case ButtonClicked(_) => {
            val f = new File(TF_rootdir.text)

            if (!(f exists))
              Dialog showMessage (null, "Please select a directory.",
                "Error", Dialog.Message.Error, null)
            else {
              val operator = new CCOperator(f, TF_divheight.text toInt,
                TF_width.text toInt, B_color.ccolor, CB_debug.selected)
              (operator go)
              Dialog.showMessage(null, "Process completed.",
                "Success", Dialog.Message.Plain, null)
              quit
            }
          }
        }
      }, constraints(2, 1))
    }
  }
}