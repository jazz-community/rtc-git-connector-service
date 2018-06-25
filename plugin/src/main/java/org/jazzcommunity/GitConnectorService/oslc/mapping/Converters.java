package org.jazzcommunity.GitConnectorService.oslc.mapping;

import ch.sbi.minigit.type.gitlab.issue.*;
import com.google.common.base.Joiner;
import org.jazzcommunity.GitConnectorService.olsc.type.issue.*;
import org.modelmapper.AbstractConverter;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class Converters {
    private static final int RTC_TIME_FACTOR = 1000;

    private Converters() {
    }

    public static AbstractConverter<Collection<String>, String> listToString() {
        return new AbstractConverter<Collection<String>, String>() {
            @Override
            protected String convert(Collection<String> strings) {
                return Joiner.on(", ").join(strings);
            }
        };
    }

    public static AbstractConverter<String, Boolean> state() {
        return new AbstractConverter<String, Boolean>() {
            @Override
            protected Boolean convert(String state) {
                return state != null;
            }
        };
    }

    public static AbstractConverter<Integer, String> toShortTitle() {
        return new AbstractConverter<Integer, String>() {
            @Override
            protected String convert(Integer iid) {
                return "Issue " + iid;
            }
        };
    }

    public static AbstractConverter<String, String> dateToUtc() {
        return new AbstractConverter<String, String>() {
            @Override
            protected String convert(String from) {
                if (from == null) {
                    return null;
                }

                LocalDate date = LocalDate.parse(from, DateTimeFormatter.ISO_DATE);
                ZonedDateTime dateTime = ZonedDateTime.of(date, LocalTime.MIDNIGHT, ZoneOffset.UTC);
                return dateTime.toString();
            }
        };
    }

    public static AbstractConverter<Integer, Integer> timeStamp() {
        return new AbstractConverter<Integer, Integer>() {
            @Override
            protected Integer convert(Integer timeStamp) {
                return timeStamp * RTC_TIME_FACTOR;
            }
        };
    }

    public static AbstractConverter<Author, DctermsContributor> authorToContributor() {
        return new AbstractConverter<Author, DctermsContributor>() {
            @Override
            protected DctermsContributor convert(Author author) {
                // We can use this approach to keep things simple and generic because the
                // rdf:type in contributor is just static data anyway. It's not beautiful,
                // but it works just fine.
                Map<Object, Object> resources = new HashMap<>();
                resources.put("rdf:resource", "http://xmlns.com/foaf/0.1/Person");
                ArrayList<Object> types = new ArrayList<>();
                types.add(resources);

                DctermsContributor contributor = new DctermsContributor();
                contributor.setRdfType(types);
                contributor.setFoafName(author.getName());
                contributor.setRdfAbout(author.getWebUrl());
                return contributor;
            }
        };
    }
}
