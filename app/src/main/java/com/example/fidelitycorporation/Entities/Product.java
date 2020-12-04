package com.example.fidelitycorporation.Entities;



import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Product {


    private int Id;
    private String Reference;
    private String ProductName;
    private String Image;
    private float Price,PromoPrice;
    private int ReductionPerc;
    private float FP;
    private int quantity;
    private String url;
    private XmlPullParserFactory xmlObject;
    public volatile boolean parsingComplete=true;

    public  Product(String url)
    {
        this.url = url;
    }

    public void parseXMLAndStoreIt(XmlPullParser myParser) throws IOException, XmlPullParserException {
        int event;
        String text;
        try {
            event  = myParser.getEventType();
            while (event!= XmlPullParser.END_DOCUMENT)
            {
                String name = myParser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        break;
                    case XmlPullParser.TEXT:
                        text = myParser.getText();
                    case XmlPullParser.END_TAG:
                        if (name.equals("name")) {
                            setProductName(name);
                        } else if (name.equals("id")) {
                            setId(Integer.parseInt(myParser.getAttributeValue(null, "value")));
                        } else if (name.equals("reference")) {
                            setReference((myParser.getAttributeValue(null, "value")));
                        } else if (name.equals("price")) {
                            setPrice(Float.parseFloat(myParser.getAttributeValue(null, "value")));
                        } else if (name.equals("reduction")) {
                            setReductionPerc(Integer.parseInt(myParser.getAttributeValue(null, "value")));
                        } else if (name.equals("fidelity")) {
                            setFP(Float.parseFloat(myParser.getAttributeValue(null, "value")));
                        } else if (name.equals("image")) {
                            setImage(myParser.getAttributeValue(null, "value"));
                        } else {

                        }
                        break;
                }
                        event =myParser.next();



                }


        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void FetchXML()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL uri = new URL(url);
                    HttpURLConnection connection = (HttpURLConnection) uri.openConnection();
                    connection.setReadTimeout(10000);
                    connection.setConnectTimeout(15000);
                    connection.setRequestMethod("GET");
                    connection.setDoInput(true);
                    connection.connect();

                    InputStream stream = connection.getInputStream();
                    xmlObject = XmlPullParserFactory.newInstance();
                    XmlPullParser myparser = xmlObject.newPullParser();
                    myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,false);
                    myparser.setInput(stream,null);
                    parseXMLAndStoreIt(myparser);
                    stream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product(int id, String reference, String productName, String image, float price, float promoPrice, int reductionPerc, float FP) {
        Id = id;
        Reference = reference;
        ProductName = productName;
        Image = image;
        Price = price;
        PromoPrice = promoPrice;
        ReductionPerc = reductionPerc;
        this.FP = FP;
    }

    public Product() {
    }

    public Product(String reference, String productName, String image, float price, float promoPrice, int reductionPerc, float FP) {
        Reference = reference;
        ProductName = productName;
        Image = image;
        Price = price;
        PromoPrice = promoPrice;
        ReductionPerc = reductionPerc;
        this.FP = FP;
    }

    public Product(String reference, String productName, String image, float price, int reductionPerc, float FP) {
        Reference = reference;
        ProductName = productName;
        Image = image;
        Price = price;
        ReductionPerc = reductionPerc;
        this.FP = FP;
    }
    public Product(String reference, String productName,float price,float promoPrice, int reductionPerc,float FP) {
        Reference = reference;
        ProductName = productName;
        Price = price;
        PromoPrice = promoPrice ;
        ReductionPerc = reductionPerc;
        this.FP = FP;

    }

    public Product(String reference, String productName,float price, int reductionPerc ) {
        Reference = reference;
        ProductName = productName;
        Price = price;
        ReductionPerc = reductionPerc;

    }
    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getReference() {
        return Reference;
    }

    public void setReference(String reference) {
        Reference = reference;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public float getPrice() {
        return Price;
    }

    public void setPrice(float price) {
        Price = price;
    }

    public float getPromoPrice() {
        return PromoPrice;
    }

    public void setPromoPrice(float promoPrice) {
        PromoPrice = promoPrice;
    }

    public int getReductionPerc() {
        return ReductionPerc;
    }

    public void setReductionPerc(int reductionPerc) {
        ReductionPerc = reductionPerc;
    }

    public float getFP() {
        return FP;
    }

    public void setFP(float FP) {
        this.FP = FP;
    }

    @Override
    public String toString() {
        return "Product{" +
                "Id=" + Id +
                ", Reference='" + Reference + '\'' +
                ", ProductName='" + ProductName + '\'' +
                ", Image='" + Image + '\'' +
                ", Price=" + Price +
                ", PromoPrice=" + PromoPrice +
                ", ReductionPerc=" + ReductionPerc +
                ", FP=" + FP +
                ", quantity=" + quantity +
                '}';
    }
}
