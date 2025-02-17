package pl.slaszu.todoapp.domain.repeat

import pl.slaszu.todoapp.domain.PresentationService
import pl.slaszu.todoapp.domain.repeat.RepeatType.RepeatTypeDay
import pl.slaszu.todoapp.domain.repeat.RepeatType.RepeatTypeMonth
import pl.slaszu.todoapp.domain.repeat.RepeatType.RepeatTypeOther
import pl.slaszu.todoapp.domain.repeat.RepeatType.RepeatTypeWeek
import java.time.Period
import javax.inject.Inject

class RepeatService @Inject constructor(
    private val presentationService: PresentationService
) {

    fun toObject(string: String): RepeatType {
        return when (val period = Period.parse(string)) {
            RepeatTypeDay().period -> RepeatTypeDay()
            RepeatTypeWeek().period -> RepeatTypeWeek()
            RepeatTypeMonth().period -> RepeatTypeMonth()
            else -> RepeatTypeOther(period)
        }
    }

}