function toggleSidebar() {
	const sidebar = document.getElementById('sidebar');
	sidebar.classList.toggle('collapsed');
	document.querySelector('.toggle-btn').classList.toggle('collapsed');
}