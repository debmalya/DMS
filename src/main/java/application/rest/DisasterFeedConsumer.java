package application.rest;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import application.model.DisasterGeoJson;
import application.model.Geometry;

@RestController
public class DisasterFeedConsumer {

	private Logger logger = Logger.getLogger("DisasterFeedConsumer");;
	private Map<String, double[]> countryMap = new HashMap<>();
	private static final long TEN_MINUTES = 1 * 60 * 1000;

	private long lastCalledOn = 0;
	private List<DisasterGeoJson> disasterList = new ArrayList<>();

	@RequestMapping("/disaster")
	@ResponseBody
	public String getFeed() throws FeedException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		populateCountryMap(mapper);

		if (System.currentTimeMillis() - lastCalledOn > TEN_MINUTES) {
			lastCalledOn = System.currentTimeMillis();
			disasterList.clear();

			String url = "https://reliefweb.int/disasters/rss.xml";
			DisasterGeoJson disasterJeoJson = new DisasterGeoJson();
			try (XmlReader reader = new XmlReader(new URL(url))) {
				SyndFeed feed = new SyndFeedInput().build(reader);
				System.out.println(feed.getTitle());
				for (SyndEntry entry : feed.getEntries()) {

					disasterJeoJson.getProperties().put("date", entry.getPublishedDate().toString());
					String title = entry.getTitle();
					String country = "";
					if (title != null && title.indexOf(":") > -1) {
						country = title.substring(0, title.indexOf(":"));
					}

					double[] countryDetails = countryMap.get(country);
					if (countryDetails != null) {
						Geometry geometry = new Geometry();
						double[] coordinates = new double[2];
						coordinates[0] = countryDetails[0];
						coordinates[1] = countryDetails[1];
						geometry.setCoordinates(coordinates);
						disasterJeoJson.setGeometry(geometry);
					}
					disasterJeoJson.getProperties().put("description", entry.getTitle());
					disasterJeoJson.getProperties().put("link", entry.getLink());
					disasterList.add(disasterJeoJson);
				}

				logger.log(Level.INFO, "Done");

			} catch (IOException ioe) {
				logger.log(Level.SEVERE, ioe.getMessage(), ioe);
			}
		}

		int index = (int) Math.random() * 1000;
		logger.log(Level.INFO, "SIZE :" + disasterList.size());
		return mapper.writeValueAsString(index % disasterList.size());
	}

	private void populateCountryMap(ObjectMapper mapper) {
		if (countryMap.isEmpty()) {
			countryMap.put("Afghanistan", new double[] { 67.709953, 33.93911 });
			countryMap.put("Albania", new double[] { 20.168331, 41.153332 });
			countryMap.put("Algeria", new double[] { 1.659626, 28.033886 });
			countryMap.put("Andorra", new double[] { 1.521801, 42.506285 });
			countryMap.put("Angola", new double[] { 17.873887, -11.202692 });
			countryMap.put("Antigua and Barbuda", new double[] { -61.796428, 17.060816 });
			countryMap.put("Argentina", new double[] { -63.61667199999999, -38.416097 });
			countryMap.put("Armenia", new double[] { 45.038189, 40.069099 });
			countryMap.put("Australia", new double[] { 133.775136, -25.274398 });
			countryMap.put("Austria", new double[] { 14.550072, 47.516231 });
			countryMap.put("Azerbaijan", new double[] { 47.576927, 40.143105 });
			countryMap.put("Bahamas", new double[] { -77.39627999999999, 25.03428 });
			countryMap.put("Bahrain", new double[] { 50.5577, 26.0667 });
			countryMap.put("Bangladesh", new double[] { 90.356331, 23.684994 });
			countryMap.put("Barbados", new double[] { -59.543198, 13.193887 });
			countryMap.put("Belarus", new double[] { 27.953389, 53.709807 });
			countryMap.put("Belgium", new double[] { 4.469936, 50.503887 });
			countryMap.put("Belize", new double[] { -88.49765, 17.189877 });
			countryMap.put("Benin", new double[] { 2.315834, 9.30769 });
			countryMap.put("Bhutan", new double[] { 90.433601, 27.514162 });
			countryMap.put("Bolivia", new double[] { -63.58865299999999, -16.290154 });
			countryMap.put("Bosnia and Herzegovina", new double[] { 17.679076, 43.915886 });
			countryMap.put("Botswana", new double[] { 24.684866, -22.328474 });
			countryMap.put("Brazil", new double[] { -51.92528, -14.235004 });
			countryMap.put("Brunei", new double[] { 114.727669, 4.535277 });
			countryMap.put("Bulgaria", new double[] { 25.48583, 42.733883 });
			countryMap.put("Burkina Faso", new double[] { -1.561593, 12.238333 });
			countryMap.put("Burundi", new double[] { 29.918886, -3.373056 });
			countryMap.put("Cabo Verde", new double[] { -23.6051721, 15.120142 });
			countryMap.put("Cambodia", new double[] { 104.990963, 12.565679 });
			countryMap.put("Cameroon", new double[] { 12.354722, 7.369721999999999 });
			countryMap.put("Canada", new double[] { -106.346771, 56.130366 });
			countryMap.put("Central African Republic", new double[] { 20.939444, 6.611110999999999 });
			countryMap.put("Chad", new double[] { 18.732207, 15.454166 });
			countryMap.put("Chile", new double[] { -71.542969, -35.675147 });
			countryMap.put("China", new double[] { 104.195397, 35.86166 });
			countryMap.put("Colombia", new double[] { -74.297333, 4.570868 });
			countryMap.put("Comoros", new double[] { 43.3333, -11.6455 });
			countryMap.put("Costa Rica", new double[] { -83.753428, 9.748916999999999 });
			countryMap.put("Cote d'Ivoire", new double[] { -5.547079999999999, 7.539988999999999 });
			countryMap.put("Croatia", new double[] { 15.2, 45.1 });
			countryMap.put("Cuba", new double[] { -77.781167, 21.521757 });
			countryMap.put("Cyprus", new double[] { 33.429859, 35.126413 });
			countryMap.put("Czech Republic", new double[] { 15.472962, 49.81749199999999 });
			countryMap.put("Denmark", new double[] { 9.501785, 56.26392 });
			countryMap.put("Djibouti", new double[] { 42.590275, 11.825138 });
			countryMap.put("Dominica", new double[] { -61.37097600000001, 15.414999 });
			countryMap.put("Dominican Republic", new double[] { -70.162651, 18.735693 });
			countryMap.put("Ecuador", new double[] { -78.18340599999999, -1.831239 });
			countryMap.put("Egypt", new double[] { 30.802498, 26.820553 });
			countryMap.put("El Salvador", new double[] { -88.89653, 13.794185 });
			countryMap.put("Equatorial Guinea", new double[] { 10.267895, 1.650801 });
			countryMap.put("Eritrea", new double[] { 39.782334, 15.179384 });
			countryMap.put("Estonia", new double[] { 25.013607, 58.595272 });
			countryMap.put("Ethiopia", new double[] { 40.489673, 9.145000000000001 });
			countryMap.put("Fiji", new double[] { 178.065032, -17.713371 });
			countryMap.put("Finland", new double[] { 25.748151, 61.92410999999999 });
			countryMap.put("France", new double[] { 2.213749, 46.227638 });
			countryMap.put("Gabon", new double[] { 11.609444, -0.803689 });
			countryMap.put("Gambia", new double[] { -15.310139, 13.443182 });
			countryMap.put("Georgia", new double[] { 43.35689199999999, 42.315407 });
			countryMap.put("Germany", new double[] { 10.451526, 51.165691 });
			countryMap.put("Ghana", new double[] { -1.023194, 7.946527 });
			countryMap.put("Greece", new double[] { 21.824312, 39.074208 });
			countryMap.put("Grenada", new double[] { -61.67899999999999, 12.1165 });
			countryMap.put("Guatemala", new double[] { -90.23075899999999, 15.783471 });
			countryMap.put("Guinea", new double[] { -9.696645, 9.945587 });
			countryMap.put("Guinea-Bissau", new double[] { -15.180413, 11.803749 });
			countryMap.put("Guyana", new double[] { -58.93018, 4.860416 });
			countryMap.put("Haiti", new double[] { -72.285215, 18.971187 });
			countryMap.put("Honduras", new double[] { -86.241905, 15.199999 });
			countryMap.put("Hungary", new double[] { 19.503304, 47.162494 });
			countryMap.put("Iceland", new double[] { -19.020835, 64.963051 });
			countryMap.put("India", new double[] { 78.96288, 20.593684 });
			countryMap.put("Indonesia", new double[] { 113.921327, -0.789275 });
			countryMap.put("Iran", new double[] { 53.688046, 32.427908 });
			countryMap.put("Iraq", new double[] { 43.679291, 33.223191 });
			countryMap.put("Ireland", new double[] { -8.24389, 53.41291 });
			countryMap.put("Israel", new double[] { 34.851612, 31.046051 });
			countryMap.put("Italy", new double[] { 12.56738, 41.87194 });
			countryMap.put("Jamaica", new double[] { -77.297508, 18.109581 });
			countryMap.put("Japan", new double[] { 138.252924, 36.204824 });
			countryMap.put("Jordan", new double[] { 36.238414, 30.585164 });
			countryMap.put("Kazakhstan", new double[] { 66.923684, 48.019573 });
			countryMap.put("Kenya", new double[] { 37.906193, -0.023559 });
			countryMap.put("Kiribati", new double[] { -157.3630262, 1.8708833 });
			countryMap.put("Kosovo", new double[] { 20.902977, 42.6026359 });
			countryMap.put("Kuwait", new double[] { 47.481766, 29.31166 });
			countryMap.put("Kyrgyzstan", new double[] { 74.766098, 41.20438 });
			countryMap.put("Laos", new double[] { 102.495496, 19.85627 });
			countryMap.put("Latvia", new double[] { 24.603189, 56.879635 });
			countryMap.put("Lebanon", new double[] { 35.862285, 33.854721 });
			countryMap.put("Lesotho", new double[] { 28.233608, -29.609988 });
			countryMap.put("Liberia", new double[] { -9.429499000000002, 6.428055 });
			countryMap.put("Libya", new double[] { 17.228331, 26.3351 });
			countryMap.put("Liechtenstein", new double[] { 9.555373, 47.166 });
			countryMap.put("Lithuania", new double[] { 23.881275, 55.169438 });
			countryMap.put("Luxembourg", new double[] { 6.129582999999999, 49.815273 });
			countryMap.put("Macedonia", new double[] { 21.745275, 41.608635 });
			countryMap.put("Madagascar", new double[] { 46.869107, -18.766947 });
			countryMap.put("Malawi", new double[] { 34.301525, -13.254308 });
			countryMap.put("Malaysia", new double[] { 101.975766, 4.210484 });
			countryMap.put("Maldives", new double[] { 73.5361034, 1.977247 });
			countryMap.put("Mali", new double[] { -3.996166, 17.570692 });
			countryMap.put("Malta", new double[] { 14.375416, 35.937496 });
			countryMap.put("Marshall Islands", new double[] { 171.989379, 6.068393599999999 });
			countryMap.put("Mauritania", new double[] { -10.940835, 21.00789 });
			countryMap.put("Mauritius", new double[] { 57.55215200000001, -20.348404 });
			countryMap.put("Mexico", new double[] { -102.552784, 23.634501 });
			countryMap.put("Micronesia", new double[] { 158.2150717, 6.8874813 });
			countryMap.put("Moldova", new double[] { 28.369885, 47.411631 });
			countryMap.put("Monaco", new double[] { 7.424615799999999, 43.73841760000001 });
			countryMap.put("Mongolia", new double[] { 103.846656, 46.862496 });
			countryMap.put("Montenegro", new double[] { 19.37439, 42.708678 });
			countryMap.put("Morocco", new double[] { -7.092619999999999, 31.791702 });
			countryMap.put("Mozambique", new double[] { 35.529562, -18.665695 });
			countryMap.put("Myanmar (Burma)", new double[] { 95.956223, 21.913965 });
			countryMap.put("Namibia", new double[] { 18.49041, -22.95764 });
			countryMap.put("Nauru", new double[] { 166.931503, -0.522778 });
			countryMap.put("Nepal", new double[] { 84.12400799999999, 28.394857 });
			countryMap.put("Netherlands", new double[] { 5.291265999999999, 52.132633 });
			countryMap.put("New Zealand", new double[] { 174.885971, -40.900557 });
			countryMap.put("Nicaragua", new double[] { -85.207229, 12.865416 });
			countryMap.put("Niger", new double[] { 8.081666, 17.607789 });
			countryMap.put("Nigeria", new double[] { 8.675277, 9.081999 });
			countryMap.put("North Korea", new double[] { 127.510093, 40.339852 });
			countryMap.put("Norway", new double[] { 8.468945999999999, 60.47202399999999 });
			countryMap.put("Oman", new double[] { 55.923255, 21.512583 });
			countryMap.put("Pakistan", new double[] { 69.34511599999999, 30.375321 });
			countryMap.put("Palau", new double[] { 134.58252, 7.514979999999999 });
			countryMap.put("Palestine", new double[] { 35.233154, 31.952162 });
			countryMap.put("Panama", new double[] { -80.782127, 8.537981 });
			countryMap.put("Papua New Guinea", new double[] { 143.95555, -6.314992999999999 });
			countryMap.put("Paraguay", new double[] { -58.443832, -23.442503 });
			countryMap.put("Peru", new double[] { -75.015152, -9.189967 });
			countryMap.put("Philippines", new double[] { 121.774017, 12.879721 });
			countryMap.put("Poland", new double[] { 19.145136, 51.919438 });
			countryMap.put("Portugal", new double[] { -8.224454, 39.39987199999999 });
			countryMap.put("Qatar", new double[] { 51.183884, 25.354826 });
			countryMap.put("Romania", new double[] { 24.96676, 45.943161 });
			countryMap.put("Russia", new double[] { 105.318756, 61.52401 });
			countryMap.put("Rwanda", new double[] { 29.873888, -1.940278 });
			countryMap.put("St. Kitts and Nevis", new double[] { -62.782998, 17.357822 });
			countryMap.put("St. Lucia", new double[] { -60.978893, 13.909444 });
			countryMap.put("St. Vincent and The Grenadines", new double[] { -61.19716279999999, 13.2528179 });
			countryMap.put("Samoa", new double[] { -172.104629, -13.759029 });
			countryMap.put("San Marino", new double[] { 12.457777, 43.94236 });
			countryMap.put("Sao Tome and Principe", new double[] { 6.613080999999999, 0.18636 });
			countryMap.put("Saudi Arabia", new double[] { 45.079162, 23.885942 });
			countryMap.put("Senegal", new double[] { -14.452362, 14.497401 });
			countryMap.put("Serbia", new double[] { 21.005859, 44.016521 });
			countryMap.put("Seychelles", new double[] { 55.491977, -4.679574 });
			countryMap.put("Sierra Leone", new double[] { -11.779889, 8.460555 });
			countryMap.put("Singapore", new double[] { 103.819836, 1.352083 });
			countryMap.put("Slovakia", new double[] { 19.699024, 48.669026 });
			countryMap.put("Slovenia", new double[] { 14.995463, 46.151241 });
			countryMap.put("Solomon Islands", new double[] { 160.156194, -9.64571 });
			countryMap.put("Somalia", new double[] { 46.199616, 5.152149 });
			countryMap.put("South Africa", new double[] { 22.937506, -30.559482 });
			countryMap.put("South Korea", new double[] { 127.766922, 35.907757 });
			countryMap.put("South Sudan", new double[] { 31.3069788, 6.876991899999999 });
			countryMap.put("Spain", new double[] { -3.74922, 40.46366700000001 });
			countryMap.put("Sri Lanka", new double[] { 80.77179699999999, 7.873053999999999 });
			countryMap.put("Sudan", new double[] { 30.217636, 12.862807 });
			countryMap.put("Suriname", new double[] { -56.027783, 3.919305 });
			countryMap.put("Swaziland", new double[] { 31.465866, -26.522503 });
			countryMap.put("Sweden", new double[] { 18.643501, 60.12816100000001 });
			countryMap.put("Switzerland", new double[] { 8.227511999999999, 46.818188 });
			countryMap.put("Syria", new double[] { 38.996815, 34.80207499999999 });
			countryMap.put("Taiwan", new double[] { 120.960515, 23.69781 });
			countryMap.put("Tajikistan", new double[] { 71.276093, 38.861034 });
			countryMap.put("Tanzania", new double[] { 34.888822, -6.369028 });
			countryMap.put("Thailand", new double[] { 100.992541, 15.870032 });
			countryMap.put("Timor-Leste", new double[] { 125.727539, -8.874217 });
			countryMap.put("Togo", new double[] { 0.824782, 8.619543 });
			countryMap.put("Tonga", new double[] { -175.198242, -21.178986 });
			countryMap.put("Trinidad and Tobago", new double[] { -61.222503, 10.691803 });
			countryMap.put("Tunisia", new double[] { 9.537499, 33.886917 });
			countryMap.put("Turkey", new double[] { 35.243322, 38.963745 });
			countryMap.put("Turkmenistan", new double[] { 59.556278, 38.969719 });
			countryMap.put("Tuvalu", new double[] { 178.6799214, -7.478441800000001 });
			countryMap.put("Uganda", new double[] { 32.290275, 1.373333 });
			countryMap.put("Ukraine", new double[] { 31.16558, 48.379433 });
			countryMap.put("United Arab Emirates", new double[] { 53.847818, 23.424076 });
			countryMap.put("United Kingdom", new double[] { -3.435973, 55.378051 });
			countryMap.put("United States of America", new double[] { -95.712891, 37.09024 });
			countryMap.put("Uruguay", new double[] { -55.765835, -32.522779 });
			countryMap.put("Uzbekistan", new double[] { 64.585262, 41.377491 });
			countryMap.put("Vanuatu", new double[] { 166.959158, -15.376706 });
			countryMap.put("Vatican City", new double[] { 12.453389, 41.902916 });
			countryMap.put("Venezuela", new double[] { -66.58973, 6.42375 });
			countryMap.put("Vietnam", new double[] { 108.277199, 14.058324 });
			countryMap.put("Yemen", new double[] { 48.516388, 15.552727 });
			countryMap.put("Zambia", new double[] { 27.849332, -13.133897 });
			countryMap.put("Zimbabwe", new double[] { 29.154857, -19.015438 });

		}

	}
}
