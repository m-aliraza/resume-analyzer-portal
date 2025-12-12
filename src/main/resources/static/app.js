const API_BASE = '/api';

// DOM Elements
const loginForm = document.getElementById('login-form');
const registerForm = document.getElementById('register-form');
const authSection = document.getElementById('auth-section');
const dashboardSection = document.getElementById('dashboard-section');
const loginContainer = document.getElementById('login-form-container');
const registerContainer = document.getElementById('register-form-container');
const errorDisplay = document.createElement('div');
errorDisplay.style.color = 'red';
errorDisplay.style.marginBottom = '10px';
errorDisplay.style.textAlign = 'center';
authSection.prepend(errorDisplay);

function displayError(message) {
    errorDisplay.textContent = message;
    errorDisplay.style.display = 'block';
}

function clearError() {
    errorDisplay.textContent = '';
    errorDisplay.style.display = 'none';
}

// Check auth on load
document.addEventListener('DOMContentLoaded', () => {
    const token = localStorage.getItem('token');
    if (token) {
        showDashboard();
    } else {
        loginContainer.style.display = 'block';
        registerContainer.style.display = 'none';
    }
});

// Toggle Login/Register
document.getElementById('show-register-link').addEventListener('click', (e) => {
    e.preventDefault();
    clearError();
    loginContainer.style.display = 'none';
    registerContainer.style.display = 'block';
});

document.getElementById('show-login-link').addEventListener('click', (e) => {
    e.preventDefault();
    clearError();
    registerContainer.style.display = 'none';
    loginContainer.style.display = 'block';
});

// Login Logic
loginForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    clearError();
    const username = document.getElementById('login-username').value;
    const password = document.getElementById('login-password').value;

    try {
        const response = await fetch(`${API_BASE}/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ usernameOrEmail: username, password: password })
        });

        if (response.ok) {
            const data = await response.json();
            localStorage.setItem('token', data.token);
            localStorage.setItem('username', data.username);
            localStorage.setItem('role', data.roles[0]);
            localStorage.setItem('userId', data.userId);
            showDashboard();
        } else {
            const errorData = await response.json();
            const errorMessage = errorData.message || 'Login failed: Unknown error';
            displayError(errorMessage);
        }
    } catch (error) {
        console.error('Error:', error);
        displayError('Login error: Could not connect to server or parse response.');
    }
});

// Register Logic
registerForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    clearError();
    const username = document.getElementById('reg-username').value;
    const email = document.getElementById('reg-email').value;
    const password = document.getElementById('reg-password').value;
    const role = document.getElementById('reg-role').value;

    try {
        const response = await fetch(`${API_BASE}/auth/register`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, email, password, role })
        });

        if (response.ok) {
            alert('Registration successful! Please login.');
            registerContainer.style.display = 'none';
            loginContainer.style.display = 'block';
        } else {
            const errorData = await response.json();
            let errorMessage = 'Registration failed: ';
            if (errorData.errors) {
                errorMessage += errorData.errors.map(err => err.defaultMessage || err.message).join('; ');
            } else if (errorData.message) {
                errorMessage += errorData.message;
            } else {
                errorMessage += 'Unknown error';
            }
            displayError(errorMessage);
        }
    } catch (error) {
        console.error('Error:', error);
        displayError('Registration error: Could not connect to server or parse response.');
    }
});

// Dashboard Logic
function showDashboard() {
    authSection.style.display = 'none';
    dashboardSection.style.display = 'block';
    
    const username = localStorage.getItem('username');
    const role = localStorage.getItem('role');
    document.getElementById('welcome-msg').innerText = `Welcome, ${username} (${role})`;

    if (role === 'ROLE_CANDIDATE') {
        document.getElementById('candidate-panel').style.display = 'block';
        loadActiveJobs();
    } else if (role === 'ROLE_RECRUITER') {
        document.getElementById('recruiter-panel').style.display = 'block';
    } else if (role === 'ROLE_ADMIN') {
        document.getElementById('admin-panel').style.display = 'block';
    }
}

// Logout
document.getElementById('logout-btn').addEventListener('click', () => {
    localStorage.clear();
    location.reload();
});

// Candidate: Upload Resume
const resumeUploadForm = document.getElementById('resume-upload-form');
if (resumeUploadForm) {
    resumeUploadForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        clearError();
        const fileInput = document.getElementById('resume-file');
        const formData = new FormData();
        formData.append('file', fileInput.files[0]);

        const token = localStorage.getItem('token');

        try {
            const response = await fetch(`${API_BASE}/candidates/resume`, {
                method: 'POST',
                headers: { 'Authorization': `Bearer ${token}` },
                body: formData
            });

            if (response.ok) {
                alert('Resume uploaded successfully!');
            } else {
                const errorData = await response.json();
                alert(errorData.message || 'Resume upload failed');
            }
        } catch (error) {
            console.error(error);
            alert('Resume upload error');
        }
    });
}

// Candidate: Load Jobs & Apply
document.getElementById('refresh-jobs-btn').addEventListener('click', loadActiveJobs);

async function loadActiveJobs() {
    try {
        const response = await fetch(`${API_BASE}/companies/jobs/active`);
        if (response.ok) {
            const page = await response.json();
            const jobs = page.content;

            const list = document.getElementById('job-list');
            list.innerHTML = '';
            if (jobs && jobs.length > 0) {
                jobs.forEach(job => {
                    const li = document.createElement('li');
                    li.style.display = 'flex';
                    li.style.justifyContent = 'space-between';
                    li.style.alignItems = 'center';
                    
                    const span = document.createElement('span');
                    span.innerText = `[${job.id}] ${job.title} - ${job.location}`;
                    
                    const btn = document.createElement('button');
                    btn.innerText = 'Apply';
                    btn.style.padding = '5px 10px';
                    btn.onclick = () => applyToJob(job.id);

                    li.appendChild(span);
                    li.appendChild(btn);
                    list.appendChild(li);
                });
            } else {
                list.innerHTML = '<li>No active jobs found.</li>';
            }
        }
    } catch (error) {
        console.error(error);
    }
}

async function applyToJob(jobId) {
    const candidateId = localStorage.getItem('userId');
    const token = localStorage.getItem('token');
    try {
        const response = await fetch(`${API_BASE}/applications/apply?candidateId=${candidateId}&jobId=${jobId}`, {
            method: 'POST',
            headers: { 
                'Authorization': `Bearer ${token}` 
            }
        });

        if (response.ok) {
            alert('Applied successfully!');
        } else {
            const err = await response.json();
            alert(err.message || 'Application failed');
        }
    } catch (error) {
        console.error(error);
        alert('Error applying');
    }
}


// Recruiter: Post Job
const postJobForm = document.getElementById('post-job-form');
if (postJobForm) {
    postJobForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const title = document.getElementById('job-title').value;
        const description = document.getElementById('job-desc').value;
        const location = document.getElementById('job-location').value;
        const companyId = document.getElementById('job-company-id').value;
        const token = localStorage.getItem('token');

        try {
            const response = await fetch(`${API_BASE}/companies/${companyId}/jobs`, {
                method: 'POST',
                headers: { 
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify({ title, description, location, companyId: parseInt(companyId) })
            });

            if (response.ok) {
                alert('Job posted!');
            } else {
                const err = await response.json();
                alert(err.message || 'Failed to post job');
            }
        } catch (error) {
            console.error(error);
        }
    });
}

// Recruiter: Review Applicants
document.getElementById('load-applicants-btn').addEventListener('click', loadApplicants);
document.getElementById('apply-filters-btn').addEventListener('click', loadApplicants);

async function loadApplicants() {
    const jobId = document.getElementById('review-job-id').value;
    if (!jobId) {
        alert('Please enter a Job ID');
        return;
    }

    const skillFilter = document.getElementById('filter-skill').value;
    const gpaFilter = document.getElementById('filter-gpa').value;
    const token = localStorage.getItem('token');

    let url = `${API_BASE}/applications/job/${jobId}/applicants?`;
    if (skillFilter) url += `skill=${skillFilter}&`;
    if (gpaFilter) url += `gpa=${gpaFilter}&`;

    try {
        const response = await fetch(url, {
            headers: { 'Authorization': `Bearer ${token}` }
        });

        if (response.ok) {
            const applicants = await response.json();
            const list = document.getElementById('applicant-list');
            list.innerHTML = '';

            if (applicants.length === 0) {
                list.innerHTML = '<li>No applicants found matching criteria.</li>';
                return;
            }

            applicants.forEach(app => {
                const li = document.createElement('li');
                li.innerHTML = `
                    <strong>${app.candidateFullName}</strong> <br>
                    Status: ${app.status} <br>
                    Applied: ${new Date(app.applicationDate).toLocaleDateString()} <br>
                    <div style="margin-top:5px;">
                        <select id="status-${app.id}">
                            <option value="REVIEWED">Reviewed</option>
                            <option value="INTERVIEWING">Interviewing</option>
                            <option value="HIRED">Hired</option>
                            <option value="REJECTED">Rejected</option>
                        </select>
                        <button onclick="updateStatus(${app.id})" style="font-size:0.8rem; padding:2px 5px;">Update</button>
                    </div>
                `;
                list.appendChild(li);
            });
        } else {
            alert('Failed to load applicants');
        }
    } catch (error) {
        console.error(error);
    }
}

window.updateStatus = async function(appId) {
    const newStatus = document.getElementById(`status-${appId}`).value;
    const token = localStorage.getItem('token');

    try {
        const response = await fetch(`${API_BASE}/applications/${appId}/status`, {
            method: 'PATCH',
            headers: { 
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({ newStatus: newStatus, notes: "Status updated via dashboard" })
        });

        if (response.ok) {
            alert('Status updated!');
            loadApplicants(); // Refresh list
        } else {
            alert('Update failed');
        }
    } catch (error) {
        console.error(error);
    }
};

// Admin: Create Company
const createCompanyForm = document.getElementById('create-company-form');
if (createCompanyForm) {
    createCompanyForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        
        // Collect all fields
        const name = document.getElementById('company-name').value;
        const website = document.getElementById('company-website').value;
        const description = document.getElementById('company-description').value;
        const street = document.getElementById('company-street').value;
        const city = document.getElementById('company-city').value;
        const state = document.getElementById('company-state').value;
        const zipCode = document.getElementById('company-zip').value;
        const country = document.getElementById('company-country').value;

        const token = localStorage.getItem('token');

        try {
            const response = await fetch(`${API_BASE}/companies`, {
                method: 'POST',
                headers: { 
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify({ 
                    name, 
                    website, 
                    description, 
                    street, 
                    city, 
                    state, 
                    zipCode, 
                    country 
                })
            });

            if (response.ok) {
                alert('Company created successfully!');
                // Optional: Clear form
                createCompanyForm.reset();
            } else {
                const err = await response.json();
                alert(err.message || 'Failed to create company');
            }
        } catch (error) {
            console.error(error);
        }
    });
}