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
     * "depth"         : Number, the depth of the nodes of the rastered image;
     *                    can also be interpreted as the length of the numbers in the image
     *                    string. <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        // System.out.println(params);
        Map<String, Object> results = new HashMap<>();

        double d_resolution = (params.get("lrlon") - params.get("ullon")) / params.get("w");
        double current_resolution = (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / MapServer.TILE_SIZE;
        int d_depth = 0;
        for (int i = 0; i < 8; i += 1) {
            if (current_resolution <= d_resolution || i == 7) {
                d_depth = i;
                break;
            }
            current_resolution = current_resolution / 2;
        }

        double map_increments_lon = (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / (Math.pow(2, d_depth));
        double map_increments_lat = (MapServer.ROOT_LRLAT - MapServer.ROOT_ULLAT) / (Math.pow(2, d_depth));

        int lrindex_lon = 0;
        double current_loc_lon = MapServer.ROOT_ULLON + map_increments_lon;
        while (current_loc_lon < params.get("lrlon")) {
            lrindex_lon += 1;
            current_loc_lon += map_increments_lon;
        }

        int ulindex_lon = 0;
        current_loc_lon = MapServer.ROOT_ULLON + map_increments_lon;
        while (current_loc_lon < params.get("ullon")) {
            ulindex_lon += 1;
            current_loc_lon += map_increments_lon;
        }

        int lrindex_lat = 0;
        double current_loc_lat = MapServer.ROOT_ULLAT + map_increments_lat;
        while (current_loc_lat > params.get("lrlat")) {
            lrindex_lat += 1;
            current_loc_lat += map_increments_lat;
        }

        int ulindex_lat = 0;
        current_loc_lat = MapServer.ROOT_ULLAT + map_increments_lat;
        while (current_loc_lat > params.get("ullat")) {
            ulindex_lat += 1;
            current_loc_lat += map_increments_lat;
        }

        String[][] files = new String[lrindex_lat - ulindex_lat + 1][lrindex_lon - ulindex_lon + 1];
        int ycounter = -1;
        for (int y = ulindex_lat; y < lrindex_lat + 1; y += 1) {
            ycounter += 1;
            int xcounter = -1;
            for (int x = ulindex_lon; x < lrindex_lon + 1; x += 1) {
                xcounter += 1;
                files[ycounter][xcounter] = "d" + d_depth + "_x" + x + "_y" + y + ".png";
            }
        }

        results.put("render_grid", files);
        results.put("raster_ul_lon", MapServer.ROOT_ULLON + (ulindex_lon) * map_increments_lon);
        results.put("raster_lr_lon", MapServer.ROOT_ULLON + (lrindex_lon + 1) * map_increments_lon);
        results.put("raster_ul_lat", MapServer.ROOT_ULLAT + (ulindex_lat) * map_increments_lat);
        results.put("raster_lr_lat", MapServer.ROOT_ULLAT + (lrindex_lat + 1) * map_increments_lat);
        results.put("depth", d_depth);
        results.put("query_success", true);

        return results;
    }

}
