package controllers

import play.api._
import libs.iteratee.Enumerator
import play.api.mvc._

import play.api.data._
import play.api.data.Forms._
import models.Task

import anorm._
import anorm.SqlParser._

import play.api.db._
import play.api.Play.current
import com.nevilon.firefly.core.{MongoWrapper, QueryResult, ESWrapper}
import com.mongodb.DBObject

object Application extends Controller {


  val mongo = new MongoWrapper()
  mongo.connect()

  def tasks = Action {
    val result = ESWrapper.sendSearchQuery("artificial intelligence")

    result.hits.foreach(h => {
      h.highlight.foreach(items => {
        //name items._1
        //value items._2
        items._2.foreach(line=>{
          println(line)
        })
      })

    })


    Ok(views.html.index(result, models.Result.all(), Task.all(), taskForm))
  }

  val taskForm = Form(
    "label" -> nonEmptyText
  )

  def index = Action {
    Redirect(routes.Application.tasks)
  }


  def newTask = Action {
    implicit request =>
      taskForm.bindFromRequest.fold(
        errors => BadRequest(views.html.index(new QueryResult(), models.Result.all(), Task.all(), errors)),
        label => {
          Task.create(label)
          Redirect(routes.Application.tasks)
        }
      )
  }


  def getFileDoc(esId: String): DBObject = {
    mongo.getFileDoc(esId)
  }

  def file(id: String) = Action {

    val file = mongo.getFile(id)

    val data = file.getInputStream
    val dataContent: Enumerator[Array[Byte]] = Enumerator.fromStream(data)
    //todo - get file size!
    Ok.stream(dataContent).
      withHeaders(CONTENT_DISPOSITION -> "attachment; filename=\"paper.pdf\"").
      withHeaders(CONTENT_LENGTH -> file.getLength.toString)

  }

  def search = Action {
    implicit request =>
      taskForm.bindFromRequest.fold(
        errors => BadRequest(views.html.index(new QueryResult(), models.Result.all(), Task.all(), errors)),
        label => {
          val result = ESWrapper.sendSearchQuery(label)
          println(result.hits.size)
          Ok(views.html.index(result, models.Result.all(), Task.all(), taskForm))


          //Task.create(label)
          //Redirect(routes.Application.tasks)
        }
      )
  }


  def deleteTask(id: Long) = Action {
    Task.delete(id)
    Redirect(routes.Application.tasks)
  }

}