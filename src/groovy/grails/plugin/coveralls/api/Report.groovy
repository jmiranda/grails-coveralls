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

    // Git
    String branch
    String commit
    String username

    public String toJson() {
        def map = [
            repo_token: repo_token,
            service_job_id: service_job_id,
            service_name: service_name,
            service_number: service_number,
            source_files: source_files,
        ]

        if (commit) {
            map.put ("git", [
                    head: [
                            id: commit,
                            committer_name: username
                    ],
                    branch: branch
                ]
            )
        }


        JsonBuilder builder = new JsonBuilder(map)

        return builder.toString()
    }

}
