package pt.ulisboa.tecnico.socialsoftware.tutor.clarification.service

import spock.lang.Specification

class SubmitAnswerTest extends Specification {

    def "submit an answer"() {
        given: "a clarification request"

        then: "the answer was submitted"
        false // TODO: impl
    }

    def "submit answer to already answered request"() {
        given: "a clarification request that already has an answer"

        expect: "an exception"
        false // TODO: impl
    }

    def "submit an answer with no request"() {
        when: "submitting an answer for a null clarification request"
        expect: "an exception"
        false // TODO: impl
    }

}
