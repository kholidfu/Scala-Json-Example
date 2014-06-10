import java.io._
import net.liftweb.json.DefaultFormats
import net.liftweb.json._
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.DefaultHttpClient

object GDataParser extends App {

  implicit val formats = DefaultFormats

  // Youtube Feed Data
  // define url
  val URL = "http://gdata.youtube.com/feeds/api/videos?v=2&alt=jsonc&q=yamaha&max-results=10"
  // create a new http request
  val req = new HttpGet(URL)
  // define client
  val client = new DefaultHttpClient
  // get HTTP response
  val resp = client.execute(req)
  // get entity
  val entity = resp.getEntity

  // input into content object
  var content = ""

  if (entity != null) {
    val inputStream = entity.getContent
    content = io.Source.fromInputStream(inputStream).getLines.mkString
    inputStream.close
  }

  // parsing things
  val json = parse(content)
  val items = (json \ "data" \ "items").children
  val titles = items.map(_.extract[GData].title)

  titles.foreach(println)

}

// a case class to match the JSON data
case class GData(
  title: String
)
