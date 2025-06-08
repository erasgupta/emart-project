fetch('footer')
	.then(response => response.text())
	.then(data => {
		document.getElementById('footer-content').innerHTML = data;
	});