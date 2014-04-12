package com.cab404.moonlight.util.html_parser;

import com.cab404.moonlight.facility.ResponseFactory;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Загружает страницу и парсит теги в отдельном потоке.
 *
 * @author cab404
 */
public class HTMLTagParserThread extends Thread implements ResponseFactory.Parser {
    private final Object working_lock;
    private CopyOnWriteArrayList<String> queue;
    private TagParser parser;
    private HTMLAnalyzerThread bonded_analyzer;
    public boolean started = false;

    public CharSequence getHTML() {
        return parser.getHTML();
    }

    public TagParser getTagParser() {
        if (started)
            synchronized (working_lock) {
                return parser;
            }
        else
            return null;
    }

    public void bondWithAnalyzer(HTMLAnalyzerThread bonded_analyzer) {
        this.bonded_analyzer = bonded_analyzer;
    }

    public HTMLTagParserThread() {
        this.queue = new CopyOnWriteArrayList<>();
        working_lock = new Object();
        parser = new TagParser();
        setDaemon(true);
    }

    public void setHandler(TagParser.TagHandler handler) {
        parser.setTagHandler(handler);
    }

    @Override public void run() {
        synchronized (working_lock) {
            started = true;

            while (true) {

                if (!queue.isEmpty()) {
                    String line;

                    line = queue.remove(0);

                    if (line == null)
                        break;

                    parser.process(line + "\n");


                }
            }
            if (bonded_analyzer != null)
                bonded_analyzer.finished();
        }

    }

    @Override public boolean line(String line) {
        queue.add(line);

        return true;
    }

    @Override public void finished() {
        queue.add(null);
    }
}
