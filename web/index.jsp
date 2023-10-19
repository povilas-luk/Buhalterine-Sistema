<%--
  Created by IntelliJ IDEA.
  User: pofke
  Date: 2020-11-23
  Time: 16:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>Accounting Page</title>
  </head>
  <body>
  Accounting List <a href="${pageContext.request.contextPath}/accounting/accList">here</a><br/>
  <form action = "${pageContext.request.contextPath}/accounting/accInfo" method = "GET">
    Accounting: <input type = "text" name = "name">
    <input type = "submit" value = "Submit" />
  </form>

  <br/> User Categories <br/>
  <form action = "${pageContext.request.contextPath}/category/userCategories" method = "GET">
    User: <input type = "text" name = "name">
    <input type = "submit" value = "Submit" />
  </form>

  <br/> Category Transactions <br/>
  <form action = "${pageContext.request.contextPath}/transactions/categoryTransactions" method = "GET">
    Category Name: <input type = "text" name = "name">
    <input type = "submit" value = "Submit" />
  </form>

  <br/>
  Login <br/>
  <form action = "${pageContext.request.contextPath}/user/login" method = "POST">
    Username: <input type = "text" name = "name">
    <br />
    Password: <input type = "text" name = "psw" />
    <input type = "submit" value = "Submit" />
  </form>

  <br/> Register Consumer <br/>
  <form action = "${pageContext.request.contextPath}/user/registerConsumer" method = "POST">
    Username: <input type = "text" name = "login">
    <br />
    Password: <input type = "text" name = "psw" />
    <br />
    Fist Name: <input type = "text" name = "name" />
    <br />
    Last Name: <input type = "text" name = "surname" />
    <br />
    Email: <input type = "text" name = "email" />
    <br />
    Phone: <input type = "text" name = "phone" />
    <br />
    Address: <input type = "text" name = "address" />
    <input type = "submit" value = "Submit" />
  </form>

  <br/> Register Company <br/>
  <form action = "${pageContext.request.contextPath}/user/registerCompany" method = "POST">
    Username: <input type = "text" name = "login">
    <br />
    Password: <input type = "text" name = "psw" />
    <br />
    Fist Name: <input type = "text" name = "name" />
    <br />
    Last Name: <input type = "text" name = "surname" />
    <br />
    Email: <input type = "text" name = "email" />
    <br />
    Phone: <input type = "text" name = "phone" />
    <br />
    Address: <input type = "text" name = "address" />
    <br />
    Company: <input type = "text" name = "company" />
    <input type = "submit" value = "Submit" />
  </form>

  <br/> Create Category <br/>
  <form action = "${pageContext.request.contextPath}/category/createCategory" method = "GET">
    Name: <input type = "text" name = "name">
    <br />
    Description: <input type = "text" name = "description" />
    <input type = "submit" value = "Submit" />
  </form>

  <br/> Create SubCategory <br/>
  <form action = "${pageContext.request.contextPath}/category/createSubCategory" method = "GET">
    Parent Category: <input type = "text" name = "parentCat">
    <br />
    Name: <input type = "text" name = "name">
    <br />
    Description: <input type = "text" name = "description" />
    <input type = "submit" value = "Submit" />
  </form>

  <br/> Assign Category To User <br/>
  <form action = "${pageContext.request.contextPath}/user/assignCategoryToUser" method = "GET">
    User: <input type = "text" name = "username">
    <br />
    Category Name: <input type = "text" name = "name">
    <input type = "submit" value = "Submit" />
  </form>

  <br/> Add Income To Category <br/>
  <form action = "${pageContext.request.contextPath}/transactions/createIncome" method = "GET">
    Category: <input type = "text" name = "catName">
    <br />
    Income Name: <input type = "text" name = "name">
    <br />
    Income Description: <input type = "text" name = "description">
    <br />
    Income Money: <input type = "text" name = "money">
    <input type = "submit" value = "Submit" />
  </form>

  <br/> Add Expense To Category <br/>
  <form action = "${pageContext.request.contextPath}/transactions/createExpense" method = "GET">
    Category: <input type = "text" name = "catName">
    <br />
    Expense Name: <input type = "text" name = "name">
    <br />
    Expense Description: <input type = "text" name = "description">
    <br />
    Expense Money: <input type = "text" name = "money">
    <input type = "submit" value = "Submit" />
  </form>

  <br/> Remove Category From User <br/>
  <form action = "${pageContext.request.contextPath}/user/removeCategoryFromUser" method = "GET">
    User: <input type = "text" name = "username">
    <br />
    Category Name: <input type = "text" name = "name">
    <input type = "submit" value = "Submit" />
  </form>

  <br/> Remove Category <br/>
  <form action = "${pageContext.request.contextPath}/category/deleteCategory" method = "GET">
    Category Name: <input type = "text" name = "name">
    <input type = "submit" value = "Submit" />
  </form>

  <br/> Remove Income From Category <br/>
  <form action = "${pageContext.request.contextPath}/transactions/deleteIncome" method = "GET">
    Category: <input type = "text" name = "catName">
    <br />
    Income Name: <input type = "text" name = "name">
    <input type = "submit" value = "Submit" />
  </form>

  <br/> Remove Expense From Category <br/>
  <form action = "${pageContext.request.contextPath}/transactions/deleteExpense" method = "GET">
    Category: <input type = "text" name = "catName">
    <br />
    Expense Name: <input type = "text" name = "name">
    <input type = "submit" value = "Submit" />
  </form>

  <br/> Category Balance <br/>
  <form action = "${pageContext.request.contextPath}/category/getCategoryBalance" method = "GET">
    Category: <input type = "text" name = "name">
    <input type = "submit" value = "Submit" />
  </form>

  <br/> User Balance <br/>
  <form action = "${pageContext.request.contextPath}/category/getUserBalance" method = "GET">
    User: <input type = "text" name = "name">
    <input type = "submit" value = "Submit" />
  </form>

  </body>
</html>
