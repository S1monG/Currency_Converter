import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/* real time uppdatering av valutor genom European central bank : 
https://www.ecb.europa.eu/stats/policy_and_exchange_rates/euro_reference_exchange_rates/html/index.en.html#dev
klassen innehåller även en statisk metod för att räkna ut belopp med olika valutor */

public class Currencies {
    private static final String url = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";
    public static Map<String, Double> map = new HashMap<>();
    
    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new URL(url).openStream());

        NodeList list = doc.getElementsByTagName("Cube");

        // get time for when last updated
        Node nodeTime = list.item(1);
        Element elementTime = (Element) nodeTime;
        String lastUpdated = elementTime.getAttribute("time");

        for(int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {

                Element element = (Element) node;

                String currency = element.getAttribute("currency");
                String rate = element.getAttribute("rate");

                if (currency != null && currency.length() > 0 && rate != null && rate.length() > 0) {
                    map.put(currency, Double.parseDouble(rate));
                }

            } 
        }
        Window window = new Window(map, lastUpdated);
    }

    public static double convert (double amout, String from, String to) {
        double rateFrom = map.get(from);
        double rateTo = map.get(to);
        return (amout * rateTo / rateFrom);
    }

}
