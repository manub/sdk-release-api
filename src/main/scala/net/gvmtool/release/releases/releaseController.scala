package net.gvmtool.release.releases

import net.gvmtool.release.candidate.{CandidateGeneralRepo, CandidateNotFoundException}
import net.gvmtool.release.request.ReleaseRequest
import net.gvmtool.release.version.{Version, VersionRepo}
import net.gvmtool.status.{BadRequest, Created, ServiceUnavailable}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataAccessException
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.bind.annotation._

trait ReleaseController {

  val versionRepo: VersionRepo

  val candidateRepo: CandidateGeneralRepo

  @RequestMapping(value = Array("/release"), method = Array(POST))
  def publish(@RequestBody request: ReleaseRequest) =
    Option(candidateRepo.findByCandidate(request.getCandidate)).map { c =>
      Created(versionRepo.save(Version(request)))
    }.getOrElse(throw CandidateNotFoundException(request.getCandidate))

  @ExceptionHandler
  def handle(e: CandidateNotFoundException) = BadRequest(e.getMessage)

  @ExceptionHandler
  def handle(e: DataAccessException) = ServiceUnavailable(e.getMessage)

}

@RestController
class Releases @Autowired()(val versionRepo: VersionRepo, val candidateRepo: CandidateGeneralRepo) extends ReleaseController
