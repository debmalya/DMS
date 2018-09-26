function createRealtimeLayer(url, container) {
    return L.realtime(url, {
        interval: 10 * 1000,
        getFeatureId: function (f) {
            return f.properties.url;
        },
        cache: true,
        container: container,
        onEachFeature(f, l) {
            l.bindPopup(function () {

                if (f.properties.time) {

                    return '<h3>' + f.properties.place + '</h3>' +
                        '<p>' + new Date(f.properties.time) +
                        '<br/>Magnitude: <strong>' + f.properties.mag + '</strong></p>' +
                        '<p><a href="' + f.properties.url + '">More information</a></p>';
                } else {

                    return '<h3>' + f.properties.title + '</h3>' +
                        '<p> Place :<strong>' + f.properties.place + '</strong>' +
                        '<p>' + f.properties.status  +
                        '<p>' + f.properties.date +
                        '<p><a href="' + f.properties.link + '">More information</a></p>';
                }
            });
        }
    });
}

var map = L.map('mapid'),
    clusterGroup = L.markerClusterGroup().addTo(map),
    subgroup1 = L.featureGroup.subGroup(clusterGroup),
    subgroup2 = L.featureGroup.subGroup(clusterGroup),
    subgroup3 = L.featureGroup.subGroup(clusterGroup),
    subgroup4 = L.featureGroup.subGroup(clusterGroup),
    subgroup5 = L.featureGroup.subGroup(clusterGroup),
    realtime1 = createRealtimeLayer('/disasters', subgroup1).addTo(map);
realtime2 = createRealtimeLayer('https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_hour.geojson', subgroup2);
realtime3 = createRealtimeLayer('/updates', subgroup3);
realtime4 = createRealtimeLayer('/jobs', subgroup4);
realtime5 = createRealtimeLayer('/trainings', subgroup5);

L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', {
    attribution: '&copy; <a href="https://earthquake.usgs.gov/earthquakes/feed/v1.0/geojson.php">USGS Earthquake Hazards Program</a>, &copy; <a href="https://github.com/perliedman/leaflet-realtime">Leaflet-Realtime</a> &copy; <a href="https://reliefweb.int/disasters/rss.xml">Relief Web</a>, &copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
}).addTo(map);

L.control.layers(null, {
    'All Disasters': realtime1,
    'All Earthquakes': realtime2,
    "Latest Updates": realtime3,
    "Relief Works": realtime4,
    "Trainings": realtime5
}).addTo(map);

realtime1.once('update', function () {
    map.fitBounds(realtime1.getBounds(), {
        maxZoom: 3
    });
});