package demo

import geotrellis.vector._
import geotrellis.vector.io._

import spray.json._

object GeoTrellisVectorData {
  // This function reads in text from a local file
  def read(path: String): String = {
    val src = scala.io.Source.fromFile(path, "UTF-8")

    try {
      src.mkString
    } finally {
      src.close
    }
  }

  def main(args: Array[String]): Unit = {

    val citiesGeoJson = read("data/cities.geojson")
    val southAmericaGeoJson = read("data/south-america.geojson")

    case class CityName(name: String)

    implicit object CityNameJsonReader extends JsonReader[CityName] {
      def read(value: JsValue): CityName =
        value.asJsObject.getFields("city") match {
          case Seq(JsString(name)) =>
            CityName(name)
          case v =>
            throw new DeserializationException("CityName expected, got $v")
        }
    }

    val cities = citiesGeoJson.extractFeatures[PointFeature[CityName]]
    cities.map(_.data.name)
    println(s"Number of cities: ${cities.length}")

    case class CountryName(name: String)

    implicit object CountryNameJsonReader extends JsonReader[CountryName] {
      def read(value: JsValue): CountryName =
        value.asJsObject.getFields("admin") match {
          case Seq(JsString(name)) =>
            CountryName(name)
          case v =>
            throw new DeserializationException("CountryName expected, got $v")
        }
    }

    val countries = southAmericaGeoJson.extractFeatures[PolygonFeature[CountryName]]
    println("Countries:")
    for(c <- countries.map(_.data.name)) { println(s"  $c") }

    def calcJoin = {
      val s = System.currentTimeMillis
      var countriesToCities2: Map[PolygonFeature[CountryName], Seq[PointFeature[CityName]]] = Map()
      //val countriesToCities: Map[PolygonFeature[CountryName], Seq[PointFeature[CityName]]] =
      for(i <- 0 to 1) {
        var i = 0
        countriesToCities2 =
          (for(country <- countries) yield {
            i += 1
            val contained =
              cities
                .filter { city: PointFeature[CityName] =>
                country.contains(city)
              }
            (country, contained)
          }).toMap
      }
      val e = System.currentTimeMillis

      for((country, cities) <- countriesToCities2) {
        println(s"${country.data.name} -> ${cities.map(_.data.name)}")
      }

      println(s"Generated in ${e - s} ms")
    }

    def calcJoin2 = {
      val s = System.currentTimeMillis
      val spatialIndex = SpatialIndex(cities) { c => (c.geom.x, c.geom.y) }
      val ss = System.currentTimeMillis
      var countriesToCities3: Map[PolygonFeature[CountryName], Traversable[PointFeature[CityName]]] = Map()
      //val countriesToCities: Map[PolygonFeature[CountryName], Traversable[PointFeature[CityName]]] =
      for(i <- 0 to 1) {
        countriesToCities3 =
          (for(country <- countries) yield {
            val contained =
              spatialIndex.traversePointsInExtent(country.envelope)
                .filter { city: PointFeature[CityName] =>
                country.contains(city)
              }
            (country, contained)
          }).toMap
      }
      val e = System.currentTimeMillis

      for((country, cities) <- countriesToCities3) {
        println(s"${country.data.name} -> ${cities.map(_.data.name)}")
      }

      println(s"Generated in ${e - s} ms  ${ss - s}")
    }
  }
}
