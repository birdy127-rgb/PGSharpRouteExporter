package io.github.auag0.pgsharprouteexporter.utils

import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import java.io.StringReader

object SPParser {
    private const val TAG_STRING = "string"
    private const val TAG_LONG = "long"
    private const val TAG_INT = "int"
    private const val TAG_BOOLEAN = "boolean"
    private const val TAG_FLOAT = "float"
    private const val TAG_SET = "set"
    private const val ATTRIBUTE_NAME = "name"
    private const val ATTRIBUTE_VALUE = "value"
    private const val VALUE_NULL = "null"

    fun parseXmlText(xmlText: String): Map<String, Any?> {
        val parser: XmlPullParser = Xml.newPullParser()
        parser.setInput(StringReader(xmlText))

        val items = mutableMapOf<String, Any?>()

        var currentSetKey: String? = null
        var currentSet: MutableSet<String>? = null

        while (true) {
            val event = parser.next()
            if (event == XmlPullParser.END_DOCUMENT) break

            when (event) {
                XmlPullParser.START_TAG -> {
                    val tag = parser.name ?: continue

                    when (tag) {
                        TAG_SET -> {
                            currentSetKey = parser.getAttributeValue(null, ATTRIBUTE_NAME)
                            currentSet = mutableSetOf()
                        }

                        TAG_STRING -> {
                            val attrName = parser.getAttributeValue(null, ATTRIBUTE_NAME)
                            val attrValue = parser.getAttributeValue(null, ATTRIBUTE_VALUE)
                            val text = try {
                                parser.nextText()
                            } catch (_: Exception) {
                                null
                            }
                            val value = attrValue ?: text ?: VALUE_NULL

                            if (currentSet != null && attrName == null) {
                                if (value != VALUE_NULL) currentSet.add(value)
                            } else {
                                val key = attrName ?: continue
                                items[key] = if (value == VALUE_NULL) null else value
                            }
                        }

                        TAG_BOOLEAN, TAG_FLOAT, TAG_INT, TAG_LONG -> {
                            val key = parser.getAttributeValue(null, ATTRIBUTE_NAME) ?: continue
                            val attrValue = parser.getAttributeValue(null, ATTRIBUTE_VALUE)
                            val parsed: Any? = when (tag) {
                                TAG_BOOLEAN -> attrValue?.toBooleanStrictOrNull()
                                TAG_FLOAT -> attrValue?.toFloatOrNull()
                                TAG_INT -> attrValue?.toIntOrNull()
                                TAG_LONG -> attrValue?.toLongOrNull()
                                else -> null
                            }
                            items[key] = parsed
                        }

                        else -> {}
                    }
                }

                XmlPullParser.END_TAG -> {
                    val tag = parser.name
                    if (tag == TAG_SET) {
                        if (currentSetKey != null && currentSet != null) {
                            items[currentSetKey] = currentSet
                        }
                        currentSetKey = null
                        currentSet = null
                    }
                }
            }
        }
        return items
    }
}