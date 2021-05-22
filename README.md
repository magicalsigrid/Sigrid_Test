Work test for the lovely people over at Toca Boca!
=========

Hello Toca Boca team, hope you're having a good day so far! Here's my work test for the position as a Test Automation Engineer. If you didn't see my CV or was at the interview, here's something quick about me; My name is Sigrid and I like dogs, cooking and playing video games. If you hire me you will also hire my dog Purjo, she's the best Test Automation Dog! I'm currently working as a Senior Test Automation Engineer for Candy Crush and will celebrate my three year anniversary with them in August but I'm looking for a new adventure and a place to grow! I really hope that place will be Toca Boca!

Tht's enough about me! The task was:

_For the new Accounts system, the team wants to be able run integration tests of its services during a deploy, we'll start with the important sign up flow for new players._
_It’s a fairly complex process, and there’s been a bug which created two accounts during the flow, and the team needs to verify that it’s been fixed._

_Please validate that the contents of the final JWT token matches the Profile ID from the first response, and that the account state is not automatically approved. 
There are two API calls that need to be made, and the JWT to validate is the second one_

I decided to write the test in Java with some help from REST-Assured (https://rest-assured.io/) and the wonderful testNG framework (https://testng.org/) that I have worked with at King. It's a really simple and quick test where we create a user and make sure nothing weird happens in the background, looking at the existing API calls it looks like there's a lot of fun things that you could easily automate. For example adding friends and then update the friend status!

Once again, thank you for taking your time to look at my very simple test. Feel free to reach out to me directly on sigrid.svederoth@hotmail.com if you have any questions!
