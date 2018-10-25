package shadattonmoy.sustnavigator.admin.controller;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class WebCrawler {
    URL url;
    InputStream is = null;
    DataInputStream dis;
    String line;

    public void crawlData() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder = new StringBuilder();

                try {
                    Document doc = Jsoup.connect("https://www.sust.edu/d/cse/faculty").get();
                    String title = doc.title();
                    Elements links = doc.select("a[data-title][data-designation][data-description]");
                    Elements titles = doc.select("a[data-title]");
                    Elements designations = doc.select("a[data-designation]");
                    Elements descriptions = doc.select("a[data-description]");
                    Log.e("Total",titles.size()+"");

                    for(int i=0;i<titles.size();i++)
                    {

                        String facultyTitle = titles.get(i).attr("data-title");
                        String facultyDesignation = designations.get(i).attr("data-designation");
                        String facultyDescription = descriptions.get(i).attr("data-description");
                        Log.e("Faculty","Title "+facultyTitle+" Designation "+facultyDesignation+" Description "+facultyDescription);
                    }


                    builder.append(title).append("\n");

                    for (Element link : links) {
                        builder.append("\n").append("Link : ").append(link.attr("href"))
                                .append("\n").append("Text : ").append(link.text());
//                        Log.e("Link",builder.toString());
                    }
                } catch (IOException e) {
                    builder.append("Error : ").append(e.getMessage()).append("\n");
                }


            }

        }).start();
    }
}
