package org.spectralpowered.client.ui

import org.spectralpowered.client.Spectral
import org.spectralpowered.common.inject
import javax.swing.JMenu
import javax.swing.JMenuBar
import javax.swing.JMenuItem

class MenuBar : JMenuBar() {

    private val spectral: Spectral by inject()

    init {
        JMenu("File").also { menu ->
            JMenuItem("Exit").also { item ->
                item.addActionListener { spectral.stop() }
                menu.add(item)
            }
            this.add(menu)
        }

        JMenu("View").also { menu ->
            this.add(menu)
        }

        JMenu("Plugins").also { menu ->
            this.add(menu)
        }

        JMenu("Help").also { menu ->
            JMenuItem("About").also { item ->
                menu.add(item)
            }
            this.add(menu)
        }
    }
}