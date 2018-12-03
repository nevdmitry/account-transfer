package com.gmail.nevdmitry

import groovyx.net.http.ContentType;
import groovyx.net.http.RESTClient
import spock.lang.Specification
import spock.lang.Unroll

import com.gmail.nevdmitry.accounttransfer.api.Path

class FirstTest extends Specification {

  RESTClient restClient = new RESTClient("http://localhost:8877")

  @Unroll("#accountId balance")
  def "get account balance"() {
    when:
    def getBody = [accountId: accountId]
    def response = restClient.get(path: Path.BALACE, query: getBody)

    then:
    assert response.status == 200

    and:
    assert response.data > 0

    where:
    accountId << [123, 321, 111]
  }

  @Unroll("transfer correct amount #amount")
  def "account transfer success #amount"() {
    def sourceAccId = 123
    def targetAccId = 321

    given:
    def sourceBalance = restClient.get(path: Path.BALACE, query: [accountId: sourceAccId]).data
    def targetBalance = restClient.get(path: Path.BALACE, query: [accountId: targetAccId]).data

    when:
    def postBody = [sourceAccId: sourceAccId, targetAccId: targetAccId, amount: amount] // will be url-encoded
    def response = restClient.post(path: Path.TRANSFER, requestContentType: ContentType.JSON, body: postBody)

    then:
    assert response.status == 200
    and:
    assert round(sourceBalance - amount) == round(restClient.get(path: Path.BALACE, query: [accountId: sourceAccId]).data)
    assert round(targetBalance + amount) == round(restClient.get(path: Path.BALACE, query: [accountId: targetAccId]).data)

    where:
    amount << [0.1, 0.01, 1, 11, 1111]

  }

  @Unroll("#amount should be positive")
  def "wrong amount"() {
    given:
    restClient.handler.failure = { resp, data ->
      resp.setData(data)
      return resp
    }
    when:

    def postBody = [sourceAccId: 123, targetAccId: 321, amount: amount]
    def response = restClient.post(path: Path.TRANSFER, requestContentType: ContentType.JSON, body: postBody)

    then:
    assert response.status == 404

    where:
    amount << [0, -1, 0.0, -0.00000000000001, -Double.MIN_VALUE, -Double.MIN_NORMAL]

  }

  @Unroll("#sourceAccId should not be equal #targetAccId")
  def "check accout ids"() {
    given:
    restClient.handler.failure = { resp, data ->
      resp.setData(data)
      return resp
    }
    when:

    def postBody = [sourceAccId: sourceAccId, targetAccId: targetAccId, amount: 1]
    def response = restClient.post(path: Path.TRANSFER, requestContentType: ContentType.JSON, body: postBody)

    then:
    assert response.status == 404

    where:
    sourceAccId << [-2, 0, 1, 111111111111111]
    targetAccId << [-2, 0, 1, 111111111111111]

  }

  def "get all transactions"() {
    when:

    def response = restClient.get(path: Path.TRANSACTIONS)

    then:
    assert response.status == 200

    and: "responce have transactions"
    assert response.data.size > 0
  }

  def round(double d) {
    return d.round(2)
  }
}