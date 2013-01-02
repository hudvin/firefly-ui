package controllers

import play.api._
import play.api.mvc._

import play.api.data._
import play.api.data.Forms._
import models.Task

import anorm._
import anorm.SqlParser._

import play.api.db._
import play.api.Play.current

object Application extends Controller {


  def tasks = Action {
    Ok(views.html.index(models.Result.all(),Task.all(), taskForm))
  }

  val taskForm = Form(
    "label" -> nonEmptyText
  )

  def index = Action {
    Redirect(routes.Application.tasks)
  }


  def newTask = Action { implicit request =>
    taskForm.bindFromRequest.fold(
      errors => BadRequest(views.html.index(models.Result.all(),Task.all(), errors)),
      label => {
        Task.create(label)
        Redirect(routes.Application.tasks)
      }
    )
  }


  def deleteTask(id: Long) = Action {
    Task.delete(id)
    Redirect(routes.Application.tasks)
  }

}