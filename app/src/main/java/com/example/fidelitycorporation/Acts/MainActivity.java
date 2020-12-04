package com.example.fidelitycorporation.Acts;

import android.os.Bundle;
import android.os.Debug;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fidelitycorporation.Entities.Product;
import com.example.fidelitycorporation.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity {

    Product pr;
    TextView tv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv1 = (TextView) findViewById(R.id.textView1);

        try {

            parseXML();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }


    }

    private void parseXML() throws XmlPullParserException {
        XmlPullParserFactory parserFactory;


        try {
            parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();
            InputStream is = getAssets().open("file.xml");
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is, null);
            processParsing(parser);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processParsing(XmlPullParser parser) {
        ArrayList<Product> products = new ArrayList<>();
        try {
            int eventType = parser.getEventType();
            Product currentP = null;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String eltName = null;
                System.out.println("--------------------------------------------" +
                        "-------------------------------------");
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        eltName = parser.getName();
                        if (eltName.equals("product")) {
                            currentP = new Product();

                            System.out.println("--------------------------------------------Test6-------------------------------------");
                        } else if (currentP != null) {
                            if (eltName.equals("name")) {

                                currentP.setProductName(parser.nextText());
                                //System.out.println(parser.nextText().toStrin);
                            } else if (eltName.equals("id")) {
                                currentP.setId(Integer.parseInt(parser.nextText()));
                              //  System.out.println(parser.nextText());
                            } else if (eltName.equals("reference")) {
                                currentP.setReference(parser.nextText());
                               // System.out.println(parser.nextText());
                            } else if (eltName.equals("price")) {
                                currentP.setPrice(Float.parseFloat(parser.nextText()));
                              //  System.out.println(parser.nextText());
                            } else if (eltName.equals("reduction")) {
                                currentP.setReductionPerc(Integer.parseInt(parser.nextText()));
                               // System.out.println(parser.nextText());
                            } else if (eltName.equals("fidelity")) {
                                currentP.setFP(Float.parseFloat(parser.nextText()));
                              //  System.out.println(parser.nextText());
                            } else if (eltName.equals("image")) {
                                currentP.setImage(parser.nextText());
                              //  System.out.println(parser.nextText());
                            }
                            products.add(currentP);
                            System.out.println("--------------------------------------------Test7-------------------------------------");
                        }break;

                }
                eventType = parser.next();

            }
            System.out.println("**************Tailee :"+products.size());
            printProducts(products);

     
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void printProducts(ArrayList<Product> products) {
        StringBuilder builder = new StringBuilder();
        Set<Product> productsSet = removeDuplicates(products);
        for (Product p : productsSet)
        {
            builder.append(p.getProductName()).append("\n")
                    .append(p.getId()).append("\n")
                    .append(p.getReference()).append("\n\n");
        }
        tv1.setText(builder.toString());
    }
    public Set<Product>  removeDuplicates(List<Product> l) {
        // ... the list is already populated
        Set<Product> s = new TreeSet<Product>(new Comparator<Product>() {

            @Override
            public int compare(Product o1, Product o2) {
               if (o1.getId()==o2.getId())
                return 0;
               else
                   return 1;
            }
        });
        s.addAll(l);
      return s;
    }
}

