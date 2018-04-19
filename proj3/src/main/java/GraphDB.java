import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Alan Yao, Josh Hug
 */
public class GraphDB {
    /** Your instance variables for storing the graph. You should consider
     * creating helper classes, e.g. Node, Edge, etc. */

    public List<String> getLocationsByPrefix(String prefix) {
        try {
            List<String> cleaned = prefixGet(GraphDB.cleanString(prefix));
            List<String> result = new ArrayList<>();
            for (String i : cleaned) {
                if (!result.contains(getFromDictionary(i).name)) {
                    result.add(getFromDictionary(i).name);
                }
            }
            return result;
        } catch(NullPointerException e) {
            return null;
        }
    }

    public List<Map<String, Object>> getLocations(String locationName) {
        String loc = GraphDB.cleanString(locationName);
        List<Map<String, Object>> result = new ArrayList<>();
        int counter = 0;
        for (long v : getFromDictionary(loc).vert) {
            HashMap<String, Object> info = new HashMap<>();
            info.put("lat", getFromDictionary(loc).lons.get(counter));
            info.put("lon", getFromDictionary(loc).lats.get(counter));
            info.put("name", getFromDictionary(loc).name);
            info.put("id", v);
            result.add(info);
            counter += 1;
        }
        return result;
    }

    private Map<Long, Node> vertices = new HashMap<>();

    class Node {
        long id;
        double lon;
        double lat;
        String name;
        Map<Long, String> neighbors;

        Node(long id, double lon, double lat) {
            this.id = id;
            this.lon = lon;
            this.lat = lat;
            neighbors = new HashMap<>();
        }
    }

    class TrieNode {
        boolean exists;
        TrieNode[] links;
        List<String> allSuffix;

        TrieNode() {
            links = new TrieNode[128];
            exists = false;
            allSuffix = new ArrayList<>();
        }
    }
    private TrieNode root = new TrieNode();

    void putLocation(String loc) {
        putLocation(root, cleanString(loc), 0);
    }

    private TrieNode putLocation(TrieNode x, String loc, int d) {
        if (x == null) {
            x = new TrieNode();
        }
        if (d == loc.length()) {
            x.allSuffix.add(loc);
            x.exists = true;
            return x;
        }
        char c = loc.charAt(d);
        x.allSuffix.add(loc);
        x.links[c] = putLocation(x.links[c], loc, d + 1);
        return x;
    }

    List<String> prefixGet(String prefix) {
        TrieNode pointer = root;
        for (int i = 0; i < prefix.length(); i += 1) {
            pointer = pointer.links[prefix.charAt(i)];
        }
        try {
            return pointer.allSuffix;
        } catch (NullPointerException e) {
            return null;
        }
    }

    class FullNameLonLatAndVertex {
        String name;
        List<Long> vert;
        List<Double> lons;
        List<Double> lats;
        FullNameLonLatAndVertex(String s, long v, double lon, double lat) {
            name = s;
            vert = new ArrayList<>();
            lons = new ArrayList<>();
            lats = new ArrayList<>();
            vert.add(v);
            lons.add(lon);
            lats.add(lat);
        }
    }

    private HashMap<String, FullNameLonLatAndVertex> dictionary = new HashMap<>();

    void addToDictionary(String cleaned, String actual, long vert, double lon, double lat) {
        if (!dictionary.containsKey(cleaned)) {
            dictionary.put(cleaned, new FullNameLonLatAndVertex(actual, vert, lon, lat));
        } else {
            dictionary.get(cleaned).vert.add(vert);
            dictionary.get(cleaned).lons.add(lon);
            dictionary.get(cleaned).lats.add(lat);
        }
    }

    FullNameLonLatAndVertex getFromDictionary(String cleanedKey) {
        return dictionary.get(cleanedKey);
    }

    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        try {
            File inputFile = new File(dbPath);
            FileInputStream inputStream = new FileInputStream(inputFile);
            // GZIPInputStream stream = new GZIPInputStream(inputStream);

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputStream, gbh);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     *  Remove nodes with no connections from the graph.
     *  While this does not guarantee that any two nodes in the remaining graph are connected,
     *  we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        Map<Long, Node> newVertices = new HashMap<>();
        for (long vert : vertices()) {
            Node node = vertices.get(vert);
            if (!node.neighbors.isEmpty()) {
                newVertices.put(vert, node);
            }
        }
        vertices = newVertices;
    }

    /**
     * Returns an iterable of all vertex IDs in the graph.
     * @return An iterable of id's of all vertices in the graph.
     */
    Iterable<Long> vertices() {
        //YOUR CODE HERE, this currently returns only an empty list.
        return vertices.keySet();
    }

    /**
     * Returns ids of all vertices adjacent to v.
     * @param v The id of the vertex we are looking adjacent to.
     * @return An iterable of the ids of the neighbors of v.
     */
    Iterable<Long> adjacent(long v) {
        return vertices.get(v).neighbors.keySet();
    }

    /**
     * Returns the great-circle distance between vertices v and w in miles.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The great-circle distance between the two locations from the graph.
     */
    double distance(long v, long w) {
        return distance(lon(v), lat(v), lon(w), lat(w));
    }

    double distance(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double dphi = Math.toRadians(latW - latV);
        double dlambda = Math.toRadians(lonW - lonV);

        double a = Math.sin(dphi / 2.0) * Math.sin(dphi / 2.0);
        a += Math.cos(phi1) * Math.cos(phi2) * Math.sin(dlambda / 2.0) * Math.sin(dlambda / 2.0);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 3963 * c;
    }

    /**
     * Returns the initial bearing (angle) between vertices v and w in degrees.
     * The initial bearing is the angle that, if followed in a straight line
     * along a great-circle arc from the starting point, would take you to the
     * end point.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The initial bearing between the vertices.
     */
    double bearing(long v, long w) {
        return bearing(lon(v), lat(v), lon(w), lat(w));
    }

    double bearing(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double lambda1 = Math.toRadians(lonV);
        double lambda2 = Math.toRadians(lonW);

        double y = Math.sin(lambda2 - lambda1) * Math.cos(phi2);
        double x = Math.cos(phi1) * Math.sin(phi2);
        x -= Math.sin(phi1) * Math.cos(phi2) * Math.cos(lambda2 - lambda1);
        return Math.toDegrees(Math.atan2(y, x));
    }

    /**
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    long closest(double lon, double lat) {
        double min = Double.MAX_VALUE;
        long mini = -1;
        for (long  n : vertices.keySet()) {
            if (distance(lon(n), lat(n), lon, lat) < min) {
                mini = n;
                min = distance(lon(n), lat(n), lon, lat);
            }
        }
        return mini;
    }

    /**
     * Gets the longitude of a vertex.
     * @param v The id of the vertex.
     * @return The longitude of the vertex.
     */
    double lon(long v) {
        return vertices.get(v).lon;
    }

    /**
     * Gets the latitude of a vertex.
     * @param v The id of the vertex.
     * @return The latitude of the vertex.
     */
    double lat(long v) {
        return vertices.get(v).lat;
    }

    void addNode(long id, double lon, double lat) {
        if (!vertices.containsKey(id)) {
            vertices.put(id, new Node(id, lon, lat));
        }
    }

    void addEdge(long v0, long v1, String name) {
        vertices.get(v0).neighbors.put(v1, name);
        vertices.get(v1).neighbors.put(v0, name);
    }

    void setName(long v, String name) {
        vertices.get(v).name = name;
    }
}
