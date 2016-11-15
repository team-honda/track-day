package org.teamhonda.trackapp.server.scrapers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.*;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Gordon Tyler
 */
public class RidersChoice implements Scraper {

private static final String NAME = "Riders Choice";
private static final String URL = "http://riderschoice.ca/track-day";
private static final Pattern RE_YEAR = Pattern.compile("([0-9]{4}) TRACK DAY SCHEDULE");
private static final ThreadLocal<DateFormat> DATE_FORMAT = new ThreadLocal<DateFormat>() {
    @Override
    protected DateFormat initialValue() {
        return new SimpleDateFormat("MMM dd");
    }
};

@Override
public String getName() {
    return NAME;
}

@Override
public void scrape(EventUpdater updater) throws IOException {

    Document doc = Jsoup.connect(URL).get();
    Element schedule = doc.select("div.track_day_schedule").first();

    int year = extractYear(schedule);
    extractEvents(updater, schedule, year);
}

private void extractEvents(EventUpdater updater, Element schedule, int year) {
    Elements rows = schedule.select("div.schedule_row_dark,div.schedule_row_light");
    for (Element row : rows) {
        String date = row.select("div.schedule_date").first().text();
        String track = row.select("div.schedule_track").first().text();
        String layout = row.select("div.schedule_circuit").first().text();
        //String price = row.select("div.schedule_price").first().text();

        String normalizedDate = normalizeDate(date, year);

        updater.update(
                NAME, URL, track + " " + date, normalizedDate, track, layout
        );
    }
}

private int extractYear(Element schedule) {
    // default to current year
    int year = Calendar.getInstance().getWeekYear();

    String title = schedule.select("div.track_day_schedule_header_title").first().text();
    Matcher yearMatcher = RE_YEAR.matcher(title);
    if (yearMatcher.find()) {
        year = Integer.parseInt(yearMatcher.group(1));
    }

    return year;
}

private String normalizeDate(String date, int year) {
    try {
        Date d = DATE_FORMAT.get().parse(date);
        return "" + year + "-" + (d.getMonth() + 1) + "-" + d.getDate();
    }
    catch (ParseException e) {
        throw new RuntimeException(e);
    }
}

}
