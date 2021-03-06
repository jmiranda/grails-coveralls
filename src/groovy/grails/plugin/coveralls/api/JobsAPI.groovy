package grails.plugin.coveralls.api

import groovyx.net.http.HTTPBuilder
import org.apache.http.entity.ContentType
import org.apache.http.entity.mime.MultipartEntityBuilder

import static groovyx.net.http.Method.POST

class JobsAPI {

    static API_HOST = 'https://coveralls.io'
    static API_PATH = '/api/v1/jobs'

    def eventListener

    JobsAPI(eventListener) {
        this.eventListener = eventListener
    }

    def create(String serviceName, String serviceJobId, String repoToken, List sourceReports, Map gitData) {
        Report report = new Report(
                service_name: serviceName,
                service_job_id: serviceJobId,
                repo_token: repoToken,
                source_files: sourceReports,
                gitData:gitData)

        String json = report.toJson()


        HTTPBuilder http = new HTTPBuilder(API_HOST + API_PATH)
        println "Send POST request with JSON to " + http.uri

        http.request(POST) { req ->
            req.entity = MultipartEntityBuilder.create()
                    .addBinaryBody('json_file', json.getBytes('UTF-8'), ContentType.APPLICATION_JSON, 'json_file')
                    .build()

            response.success = { resp, reader ->
                assert resp.status == 200
                return true
            }

            response.failure = { resp, reader ->
                println resp.statusLine
                println resp.getAllHeaders()
                println resp.getData()
                System.out << reader
                eventListener?.triggerEvent("StatusError", "Could not post coverage reports: ${resp.statusLine.toString()}")
                return false
            }
        }
    }

}