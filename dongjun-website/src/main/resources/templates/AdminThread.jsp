<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"
    import = "java.util.Map"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

<pre>
	<%
		for (Map.Entry<Thread, StackTraceElement[]> stackTrace : Thread.getAllStackTraces().entrySet()) {
			Thread thread = (Thread) stackTrace.getKey();
			StackTraceElement[] stack = (StackTraceElement[]) stackTrace.getValue();
			if (thread.equals(Thread.currentThread())) {
				continue;
			}
			out.print("\n 线程: " + thread.getName() + "\n");
			for (StackTraceElement element : stack) {
				out.print("\t" + element + "\n");
			}
		}
	%>
</pre>

</body>
</html>