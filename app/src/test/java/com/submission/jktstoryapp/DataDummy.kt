package com.submission.jktstoryapp

import com.submission.jktstoryapp.data.response.ListStoryItem

object DataDummy {
    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                i.toString(),
                "photo + $i",
                "name + $i",
                "description $i",
            )
            items.add(story)
        }
        return items
    }
}

