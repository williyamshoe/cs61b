import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {

    public Rasterer() {
        // YOUR CODE HERE
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        // System.out.println(params);
        Map<String, Object> results = new HashMap<>();

        double dResolution = (params.get("lrlon") - params.get("ullon")) / params.get("w");
        double currentResolution = (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON)
                / MapServer.TILE_SIZE;
        int dDepth = 0;
        for (int i = 0; i < 8; i += 1) {
            if (currentResolution <= dResolution || i == 7) {
                dDepth = i;
                break;
            }
            currentResolution = currentResolution / 2;
        }

        double mapIncrementsLon = (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON)
                / (Math.pow(2, dDepth));
        double mapIncrementsLat = (MapServer.ROOT_LRLAT - MapServer.ROOT_ULLAT)
                / (Math.pow(2, dDepth));

        int lrindexLon = 0;
        double currentLocLon = MapServer.ROOT_ULLON + mapIncrementsLon;
        while (currentLocLon < params.get("lrlon")) {
            lrindexLon += 1;
            currentLocLon += mapIncrementsLon;
        }

        int ulindexLon = 0;
        currentLocLon = MapServer.ROOT_ULLON + mapIncrementsLon;
        while (currentLocLon < params.get("ullon")) {
            ulindexLon += 1;
            currentLocLon += mapIncrementsLon;
        }

        int lrindexLat = 0;
        double currentLocLat = MapServer.ROOT_ULLAT + mapIncrementsLat;
        while (currentLocLat > params.get("lrlat")) {
            lrindexLat += 1;
            currentLocLat += mapIncrementsLat;
        }

        int ulindexLat = 0;
        currentLocLat = MapServer.ROOT_ULLAT + mapIncrementsLat;
        while (currentLocLat > params.get("ullat")) {
            ulindexLat += 1;
            currentLocLat += mapIncrementsLat;
        }

        String[][] files = new String[lrindexLat - ulindexLat + 1][lrindexLon - ulindexLon + 1];
        int ycounter = -1;
        for (int y = ulindexLat; y < lrindexLat + 1; y += 1) {
            ycounter += 1;
            int xcounter = -1;
            for (int x = ulindexLon; x < lrindexLon + 1; x += 1) {
                xcounter += 1;
                files[ycounter][xcounter] = "d" + dDepth + "_x" + x + "_y" + y + ".png";
            }
        }

        results.put("render_grid", files);
        results.put("raster_ul_lon", MapServer.ROOT_ULLON + (ulindexLon) * mapIncrementsLon);
        results.put("raster_lr_lon", MapServer.ROOT_ULLON + (lrindexLon + 1) * mapIncrementsLon);
        results.put("raster_ul_lat", MapServer.ROOT_ULLAT + (ulindexLat) * mapIncrementsLat);
        results.put("raster_lr_lat", MapServer.ROOT_ULLAT + (lrindexLat + 1) * mapIncrementsLat);
        results.put("depth", dDepth);
        results.put("query_success", true);

        return results;
    }

}
