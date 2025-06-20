const loginPopup = document.getElementById("loginPopup");
const signupPopup = document.getElementById("signupPopup");
const overlay = document.getElementById("overlay");
const loginIcon = document.getElementById("loginIcon");

const usernameInput = document.getElementById("username");
const codeInput = document.getElementById("codeInput");
const codeSection = document.getElementById("codeSection");
const usernameError = document.getElementById("usernameError");
const responseMessage = document.getElementById("responseMessage");

const firstName = document.getElementById("firstName");
const lastName = document.getElementById("lastName");
const signupEmail = document.getElementById("signupEmail");
const signupPhone = document.getElementById("signupPhone");
const signupMessage = document.getElementById("signupMessage");

let jwtToken = '';


loginIcon.addEventListener("click", function() {
	loginPopup.style.display = "block";
	overlay.style.display = "block";
});

overlay.onclick = () => {
	overlay.style.display = "none";
	loginPopup.style.display = "none";
	signupPopup.style.display = "none";
	resetAll();
};

function closePopup() {
	loginPopup.style.display = "none";
	overlay.style.display = "none";
	usernameError.style.display = "none";
	usernameInput.value = "";
	responseMessage.style.display = "none";
	codeInput.value = "";
	codeSection.style.display = "none";
}

// Optional: Close popup if clicking outside it
overlay.addEventListener("click", closePopup);

function isValidEmail(email) {
	return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
}

function isValidPhone(phone) {
	return /^\d{10}$/.test(phone);
}

async function validateLogin() {
	const username = usernameInput.value.trim();
	usernameError.style.display = "none";
	responseMessage.style.display = "none";

	if (!isValidEmail(username) && !isValidPhone(username)) {
		usernameError.style.display = "block";
		return;
	}

	try {
		const res = await fetch("/emart/api/users/send-code", {
			method: "POST",
			headers: { "Content-Type": "application/json" },
			body: JSON.stringify({ username })
		});

		const data = await res.json();

		if (data.success) {
			responseMessage.textContent = data.message || "Code sent!";
			responseMessage.className = "message success-message";
			codeSection.style.display = "block";
		} else if (data.code === "USER_NOT_FOUND") {
			openSignup(username);
		} else {
			responseMessage.textContent = data.message || "Error sending code";
			responseMessage.className = "message error-message";
			//openSignup(username);
		}
		responseMessage.style.display = "block";
	} catch (err) {
		responseMessage.textContent = "Network error.";
		responseMessage.className = "message error-message";
		responseMessage.style.display = "block";
	}
}

async function verifyCode() {
	const username = usernameInput.value.trim();
	const code = codeInput.value.trim();
	responseMessage.style.display = "none";

	if (!code) {
		responseMessage.textContent = "Please enter the code.";
		responseMessage.className = "message error-message";
		responseMessage.style.display = "block";
		return;
	}

	try {
		const res = await fetch("/emart/api/users/verify-code", {
			method: "POST",
			headers: { "Content-Type": "application/json" },
			body: JSON.stringify({ username, code })
		});

		const data = await res.json();

		if (data.success) {
			jwtToken = data.code;
			localStorage.setItem('authToken', jwtToken);
			responseMessage.textContent = data.message || "Verified!";
			responseMessage.className = "message success-message";
			responseMessage.style.display = "block";
			getUserDetails();
			setTimeout(closePopup, 1500);
		} else {
			responseMessage.textContent = data.message || "Invalid code";
			responseMessage.className = "message error-message";
			responseMessage.style.display = "block";
		}
	} catch (err) {
		responseMessage.textContent = "Network error.";
		responseMessage.className = "message error-message";
		responseMessage.style.display = "block";
	}
}

function openSignup(prefillUsername) {
	loginPopup.style.display = "none";
	signupPopup.style.display = "block";

	if (isValidEmail(prefillUsername)) {
		signupEmail.value = prefillUsername;
	} else if (isValidPhone(prefillUsername)) {
		signupPhone.value = prefillUsername;
	}
}

async function submitSignup() {
	signupMessage.style.display = "none";
	const data = {
		firstName: firstName.value.trim(),
		lastName: lastName.value.trim(),
		email: signupEmail.value.trim(),
		phone: signupPhone.value.trim(),
	};

	if (!data.firstName || !data.lastName || !isValidEmail(data.email) || !isValidPhone(data.phone)) {
		signupMessage.textContent = "Please fill in all fields correctly.";
		signupMessage.className = "message error-message";
		signupMessage.style.display = "block";
		return;
	}

	const res = await fetch("/api/signup", {
		method: "POST",
		headers: { "Content-Type": "application/json" },
		body: JSON.stringify(data)
	});

	const result = await res.json();

	signupMessage.textContent = result.message;
	signupMessage.className = result.success ? "message success-message" : "message error-message";
	signupMessage.style.display = "block";

	if (result.success) {
		setTimeout(closeAllPopups, 1500);
	}
}

function closeAllPopups() {
	overlay.style.display = "none";
	loginPopup.style.display = "none";
	signupPopup.style.display = "none";
	resetAll();
}

function resetAll() {
	usernameInput.value = "";
	codeInput.value = "";
	firstName.value = "";
	lastName.value = "";
	signupEmail.value = "";
	signupPhone.value = "";
	codeSection.style.display = "none";
	usernameError.style.display = "none";
	responseMessage.style.display = "none";
	signupMessage.style.display = "none";
}

function getUserDetails() {
	const token = localStorage.getItem('authToken');
	if (!token) {
		alert('You must login first.');
		return;
	}

	fetch('/emart/api/users/getUserDetails', {
		method: 'GET',
		headers: {
			'Authorization': 'Bearer ' + token
		}
	})
		.then(res => {
			if (res.status === 401) throw new Error('Token expired or invalid');
			return res.json();
		})
		.then(user => {
			// document.getElementById('userDetails').innerHTML =
			// `<p><strong>Name:</strong> ${user.firstName} ${user.lastName}</p>
			// <p><strong>Email:</strong> ${user.email}</p>
			// <p><strong>Phone:</strong> ${user.phone}</p>`;

			// ✅ Set welcome message
			document.getElementById('welcomeMessage').innerText =
				`Welcome, ${user.name}`;
		})
		.catch(err => {
			alert(err.message);
		});
}

async function signUpUser() {
	signupMessage.style.display = "none";
	
	const payload = {
		firstName: firstName.value.trim(),
		lastName: lastName.value.trim(),
		email: signupEmail.value.trim(),
		phone: signupPhone.value.trim(),
	};
	
	if (!payload.firstName || !payload.lastName || !isValidEmail(payload.email) || !isValidPhone(payload.phone)) {
		signupMessage.textContent = "Please fill in all fields correctly.";
		signupMessage.className = "message error-message";
		signupMessage.style.display = "block";
		return;
	}
	
	try {
		const response = await fetch("/emart/api/users/signup", {
			method: "POST",
			headers: {
				"Content-Type": "application/json",
			},
			body: JSON.stringify(payload),
		});

		const result = await response.json();


		if (response.ok) {
			signupMessage.textContent = result.message;
			signupMessage.className = "message success-message";
			signupMessage.style.display = "block";
			setTimeout(closeAllPopups, 1500);

		} else {
			signupMessage.textContent = result.message;
			signupMessage.className = "message error-message";
			signupMessage.style.display = "block";
		}
	} catch (error) {
		console.error("Sign-up failed:", error);
		alert("⚠️ Network or server error");
	}
}

