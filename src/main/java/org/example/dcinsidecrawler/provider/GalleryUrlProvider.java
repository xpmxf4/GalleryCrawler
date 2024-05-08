package org.example.dcinsidecrawler.provider;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class GalleryUrlProvider {
    private GalleryUrlProvider() {
    }

    private static final Map<Integer, GalleryPeriod> galleryPeriods = new HashMap<>();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    static {
        galleryPeriods.put(0, new GalleryPeriod("2006-08-01", "2009-01-31"));
        galleryPeriods.put(2, new GalleryPeriod("2005-07-01", "2007-02-28"));
        galleryPeriods.put(3, new GalleryPeriod("2009-01-01", "2009-11-30"));
        // ID 4는 Permission Denied
        galleryPeriods.put(5, new GalleryPeriod("2013-02-01", "2018-06-30"));
        galleryPeriods.put(6, new GalleryPeriod("2018-06-01", "2021-02-28"));
        galleryPeriods.put(7, new GalleryPeriod("2021-02-05", "2021-11-25"));
        galleryPeriods.put(8, new GalleryPeriod("2022-11-01", "2024-04-30"));
        galleryPeriods.put(9, new GalleryPeriod("2024-04-01", null)); // ongoing
    }

    public static String getGalleryUrl(String targetDate) {
        LocalDate date = LocalDate.parse(targetDate, DATE_FORMATTER);
        Integer galleryId = findGalleryId(date);

        if (galleryId == null) {
            throw new IllegalArgumentException("해당 날짜에 유효한 갤러리가 없습니다.");
        }

        String idSuffix = galleryId == 0 ? "" : String.valueOf(galleryId);
        return "http://gall.dcinside.com/board/lists/?id=football_new" + idSuffix + "&list_num=100&page=";
    }

    private static Integer findGalleryId(LocalDate date) {
        for (Map.Entry<Integer, GalleryPeriod> entry : galleryPeriods.entrySet()) {
            if (entry.getValue().isDateWithinPeriod(date)) {
                return entry.getKey();
            }
        }
        return null; // No valid gallery found
    }

    private static class GalleryPeriod {
        private final LocalDate startDate;
        private final LocalDate endDate;

        public GalleryPeriod(String start, String end) {
            this.startDate = start == null ? LocalDate.MIN : LocalDate.parse(start, DATE_FORMATTER);
            this.endDate = end == null ? LocalDate.MAX : LocalDate.parse(end, DATE_FORMATTER);
        }

        public boolean isDateWithinPeriod(LocalDate date) {
            return !date.isBefore(startDate) && !date.isAfter(endDate);
        }
    }
}

