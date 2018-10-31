package ru.xitrix.uikit.Example.Data;

import android.os.Handler;
import android.os.Looper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GenresListContent {
    public static class GenreItem implements Serializable {
        public final String title;
        public final String link;

        GenreItem(String title, String link) {
            this.title = title;
            this.link = link;
        }
    }

    public static void getGenresListItems(final OnCompletion completion) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final List<GenreItem> result = new ArrayList<>();
                    Document doc = Jsoup.connect("http://findanime.me/list").get();

                    Elements elements = doc.select("div.rightContent").select("div.rightBlock");

                    for (Element element : elements) {
                        for (Element genre : element.select("li")) {
                            String link;
                            if (!genre.text().isEmpty() && !genre.text().equals("Все") && !(link = genre.select("a").attr("href")).isEmpty()) {
                                String title = genre.text().substring(0, 1).toUpperCase() + genre.text().substring(1);
                                result.add(new GenreItem(title, link));
                            }
                        }
                    }

                    (new Handler(Looper.getMainLooper())).post(new Runnable() {
                        @Override
                        public void run() {
                            completion.onCompletion(result);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public interface OnCompletion {
        void onCompletion(List<GenreItem> list);
    }
}
