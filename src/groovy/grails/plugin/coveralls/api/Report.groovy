package grails.plugin.coveralls.api

import grails.plugin.coveralls.coverage.SourceReport
import groovy.json.JsonBuilder

/**
 * The model class of the report for Coveralls' format.
 */
class Report {

	String service_job_id
	String service_name
    String service_number
	String repo_token
	List<SourceReport> source_files
    Map gitData


    public String toJson() {
        def map = [
            repo_token: repo_token,
            service_job_id: service_job_id,
            service_name: service_name,
            service_number: service_number,
            source_files: source_files,
        ]

        if (gitData?.commit) {
            map.put ("git", [
                    head: [
                            id: gitData.commit,
                            author_name: gitData.authorName,
                            author_email: gitData.authorEmail,
                            committer_name:gitData.committerName,
                            committer_email: gitData.committerEmail,
                            message: gitData.message
                    ],
                    branch: gitData.branch
                ]
            )
        }

        JsonBuilder builder = new JsonBuilder(map)

        return builder.toString()
    }

}
