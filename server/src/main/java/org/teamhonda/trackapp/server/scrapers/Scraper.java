package org.teamhonda.trackapp.server.scrapers;

import java.io.IOException;

/**
 * @author Gordon Tyler
 */
public interface Scraper {

String getName();
void scrape(EventUpdater updater) throws IOException;

}
