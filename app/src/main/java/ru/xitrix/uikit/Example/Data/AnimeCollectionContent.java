package ru.xitrix.uikit.Example.Data;

import android.os.Handler;
import android.os.Looper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AnimeCollectionContent {

    public static class AnimeItem implements Serializable {
        public final String title;
        public final String details;
        public final String image;
        public final String link;

        public AnimeItem(String title, String details, String image, String link) {
            this.title = title;
            this.details = details;
            this.image = image;
            this.link = link;
        }

        @Override
        public String toString() {
            return title;
        }
    }

    public static void getAnimeCollectionItems(final String link, final OnCompletion completion) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final List<AnimeItem> result = new ArrayList<>();
                    Document doc = Jsoup.connect(link).get();

                    Elements cells = doc.select("div.tile.col-sm-6");
                    for (Element cell : cells) {
                        String name = cell.select("div.desc").select("h3").select("a").first().attr("title");
                        if (name == null) {
                            name = cell.select("div.desc").select("h4").first().attr("title");
                        }

                        String rating = cell.select("div.rating").attr("title").split(" из 10")[0];
                        float f = Float.valueOf(rating);
                        rating = "Оценка: " + round(f, 2) + "/10";

                        String image = cell.select("img.lazy").attr("data-original");

                        String link = cell.select("div.desc").select("h3").select("a").attr("href");

                        result.add(new AnimeItem(name, rating, image, link));
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

    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    public interface OnCompletion {
        void onCompletion(List<AnimeItem> list);
    }
}
