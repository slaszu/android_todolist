package pl.slaszu.todoapp.domine

import org.junit.Assert
import org.junit.Test
import pl.slaszu.todoapp.domain.FakeTodoModel
import pl.slaszu.todoapp.domain.PresentationService
import java.time.LocalDateTime

class PresentationServiceTest {
    @Test
    fun checkConvertToTimelineMap() {

        val todoList = listOf(
            FakeTodoModel(text = "no date"),
            FakeTodoModel(text = "today", startDate = LocalDateTime.now())
        )

        val timelineMap = PresentationService.convertToTimelineMap(todoList)

        Assert.assertEquals(2, timelineMap.size)

    }
}