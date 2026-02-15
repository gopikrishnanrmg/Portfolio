function setCookie(name, value, minutes) {
  const expires = new Date(Date.now() + minutes * 60 * 1000).toUTCString();
  document.cookie = `${name}=${value}; expires=${expires}; path=/; SameSite=Lax`;
}

function getCookie(name) {
  const match = document.cookie.match(new RegExp('(^| )' + name + '=([^;]+)'));
  return match ? match[2] : null;
}

function generateId() {
  return crypto.randomUUID();
}

export function getOrCreateVisitorId() {
  let id = getCookie("visitorId");
  if (!id) {
    id = generateId();
    setCookie("visitorId", id, 60 * 24 * 180);
  }
  return id;
}

export function getOrCreateSessionId() {
  let id = getCookie("sessionId");
  if (!id) {
    id = generateId();
  }
  setCookie("sessionId", id, 30);
  setCookie("session_id", id, 60 * 24 * 30); //TODO: change the name to make it less ambiguous, this is for chat
  return id;
}

export async function sendAnalyticsEvent(event) {
  
  try {
    const API_BASE_URL = window.RUNTIME_CONFIG.API_BASE_URL

    const response = await fetch(`${API_BASE_URL}/analytics/api/v1/analytics`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(event)
    });

    if (!response.ok) {
      console.error("Failed to send analytics");
    }
  } catch (err) {
    console.error("Error sending analytics:", err);
  }
}

