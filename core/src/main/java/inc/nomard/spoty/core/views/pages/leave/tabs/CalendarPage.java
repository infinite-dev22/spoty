package inc.nomard.spoty.core.views.pages.leave.tabs;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.view.CalendarView;
import inc.nomard.spoty.core.views.util.OutlinePage;
import javafx.application.Platform;
import javafx.scene.layout.BorderPane;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalTime;

@SuppressWarnings("unchecked")
@Slf4j
public class CalendarPage extends OutlinePage {

    public CalendarPage() {
        super();
        addNode(init());
    }

    public BorderPane init() {
        var pane = new BorderPane();
        pane.setCenter(buildCenter());
        return pane;
    }

    public CalendarView buildCenter() {
        var calendarView = new CalendarView(); // (1)

        var birthdays = new Calendar("Birthdays"); // (2)
        var holidays = new Calendar("Holidays");

        birthdays.setStyle(Calendar.Style.STYLE1); // (3)
        holidays.setStyle(Calendar.Style.STYLE2);

        CalendarSource myCalendarSource = new CalendarSource("My Calendars"); // (4)
        myCalendarSource.getCalendars().addAll(birthdays, holidays);

        calendarView.getCalendarSources().addAll(myCalendarSource); // (5)

        calendarView.setRequestedTime(LocalTime.now());

        Thread updateTimeThread = new Thread("Calendar: Update Time Thread") {
            @Override
            public void run() {
                while (true) {
                    Platform.runLater(() -> {
                        calendarView.setToday(LocalDate.now());
                        calendarView.setTime(LocalTime.now());
                    });

                    try {
                        // update every 10 seconds
                        sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        };

        updateTimeThread.setPriority(Thread.MIN_PRIORITY);
        updateTimeThread.setDaemon(true);
        updateTimeThread.start();
        return calendarView;
    }
}
