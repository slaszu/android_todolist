package pl.slaszu.todoapp.domine

import org.junit.Assert
import org.junit.Test
import pl.slaszu.todoapp.domain.FakeTodoModel
import pl.slaszu.todoapp.domain.PresentationService
import pl.slaszu.todoapp.domain.TimelineHeader
import java.time.LocalDateTime

class PresentationServiceTest {
    @Test
    fun checkConvertToTimelineMap() {

        val now = LocalDateTime.of(
            2025,
            3,
            10,
            12,
            30
        )

        val todoList = listOf(
            FakeTodoModel(text = "no date"),
            FakeTodoModel(text = "out of date", startDate = now.minusDays(1)),

            FakeTodoModel(text = "today", startDate = now.minusMinutes(10)),
            FakeTodoModel(text = "today", startDate = now.plusMinutes(10)),

            FakeTodoModel(text = "this week", startDate = now.plusDays(1)),
            FakeTodoModel(text = "this week", startDate = now.plusDays(6)),

            FakeTodoModel(text = "next week", startDate = now.plusDays(7)),
            FakeTodoModel(text = "next week", startDate = now.plusDays(13)),

            FakeTodoModel(text = "this month", startDate = now.plusDays(14)),
            FakeTodoModel(text = "this month", startDate = now.plusDays(30)),


            FakeTodoModel(text = "later", startDate = now.plusMonths(1)),
            FakeTodoModel(text = "later", startDate = now.plusDays(31))
        )

        val timelineMap = PresentationService.convertToTimelineMap(todoList, now)

        Assert.assertEquals(7, timelineMap.size)

        Assert.assertEquals(1, timelineMap[TimelineHeader.NO_DATE]?.size)
        Assert.assertEquals("no date", timelineMap[TimelineHeader.NO_DATE]?.first()?.text)

        Assert.assertEquals(1, timelineMap[TimelineHeader.OUT_OF_DATE]?.size)
        Assert.assertEquals("out of date", timelineMap[TimelineHeader.OUT_OF_DATE]?.first()?.text)

        Assert.assertEquals(2, timelineMap[TimelineHeader.TODAY]?.size)
        Assert.assertEquals(2, timelineMap[TimelineHeader.TODAY]?.count {
            it.text == "today"
        })

        Assert.assertEquals(2, timelineMap[TimelineHeader.THIS_WEEK]?.size)
        Assert.assertEquals(2, timelineMap[TimelineHeader.THIS_WEEK]?.count {
            it.text == "this week"
        })

        Assert.assertEquals(2, timelineMap[TimelineHeader.NEXT_WEEK]?.size)
        Assert.assertEquals(2, timelineMap[TimelineHeader.NEXT_WEEK]?.count {
            it.text == "next week"
        })

        Assert.assertEquals(2, timelineMap[TimelineHeader.THIS_MONTH]?.size)
        Assert.assertEquals(2, timelineMap[TimelineHeader.THIS_MONTH]?.count {
            it.text == "this month"
        })

        Assert.assertEquals(2, timelineMap[TimelineHeader.NEXT_MONTH_PLUS]?.size)
        Assert.assertEquals(2, timelineMap[TimelineHeader.NEXT_MONTH_PLUS]?.count {
            it.text == "later"
        })
    }
}