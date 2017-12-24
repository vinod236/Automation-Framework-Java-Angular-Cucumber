@smoke @regression
Feature: Parallelisation 003
  As a brilliant and caring individual
  I want to be able to adopt a puppy
  So that I can do a lil' good in the world

  Background:
    Puppy adoption scenarios 003
    Given The order "Order A" exists
    And I am on the homepage

	@issue=007 @tmsLink=008 @severity=critical @flaky
  Scenario: User adopts a puppy 003
    When I adopt a puppy providing "Order A"
    Then I should see the successful adoption message