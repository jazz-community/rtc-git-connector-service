package org.jazzcommunity.GitConnectorService.oslc.mapping;

import ch.sbi.minigit.type.gitlab.issue.Author;
import com.google.common.base.Joiner;
import org.jazzcommunity.GitConnectorService.olsc.type.issue.DctermsContributor;
import org.modelmapper.AbstractConverter;

import java.util.Collection;

public final class Converters {
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

    public static AbstractConverter<Author, DctermsContributor> authorToContributor() {
        return new AbstractConverter<Author, DctermsContributor>() {
            @Override
            protected DctermsContributor convert(Author author) {
                DctermsContributor contributor = new DctermsContributor();
                contributor.setFoafName(author.getName());
                contributor.setRdfAbout(author.getWebUrl());
                return contributor;
            }
        };
    }
}
