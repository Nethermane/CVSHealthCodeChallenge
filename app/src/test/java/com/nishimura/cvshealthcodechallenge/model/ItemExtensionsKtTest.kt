package com.nishimura.cvshealthcodechallenge.model

import junit.framework.TestCase
import org.junit.Assert

class ItemExtensionsKtTest : TestCase() {
    /**
     * Test the parsing of descriptions from some example HTML samples
     */
    fun testParseDescriptionFromHtml() {
        val descWithDesc =
            " <p><a href=\"https://www.flickr.com/people/bhf3737/\">BHF3737</a> posted a photo:</p> <p><a href=\"https://www.flickr.com/photos/bhf3737/51537564160/\" title=\"20210930 Porcupine\"><img src=\"https://live.staticflickr.com/65535/51537564160_dd16ae9a1c_m.jpg\" width=\"240\" height=\"160\" alt=\"20210930 Porcupine\" /></a></p> <p>A wild Porcupine cautiously looking back while resting on a tree trunk at Riverbend, #Calgary.. The hardly ever show their face while resting, so a rather unique porcupine pose.</p>"
        val expectedParsedDescWithDesc = "<p>A wild Porcupine cautiously looking back while resting on a tree trunk at Riverbend, #Calgary.. The hardly ever show their face while resting, so a rather unique porcupine pose.</p>"
        val parsedDescriptionWithDescription = Item("","","",descWithDesc,"",Media(""),"","","").parseDescriptionFromHtml()
        Assert.assertEquals(expectedParsedDescWithDesc, parsedDescriptionWithDescription)
        val descWithoutDesc =
            " <p><a href=\"https://www.flickr.com/people/nickilock/\">Nicki Lock</a> posted a photo:</p> <p><a href=\"https://www.flickr.com/photos/nickilock/51548486330/\" title=\"2X5A6744\"><img src=\"https://live.staticflickr.com/65535/51548486330_f22bdec5fd_m.jpg\" width=\"240\" height=\"160\" alt=\"2X5A6744\" /></a></p> "
        val expectedParseDescriptionWithoutDescription = null
        val parsedDescriptionWithoutDescription = Item("","","",descWithoutDesc,"",Media(""),"","","").parseDescriptionFromHtml()
        Assert.assertEquals(expectedParseDescriptionWithoutDescription, parsedDescriptionWithoutDescription)

    }
}