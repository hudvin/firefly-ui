package models

import collection.mutable.ListBuffer

/**
 * Created with IntelliJ IDEA.
 * User: hudvin
 * Date: 1/1/13
 * Time: 1:05 PM
 * To change this template use File | Settings | File Templates.
 */

case class Result(label: String)


object Result {

  var label = ""

  def all(): List[Result] = {
    val results = new ListBuffer[Result]
    for (i<-0 until 10){
       val result = new Result(i.toString)
      results+=result
    }
    return results.toList

  }

}
