export const setLocalStorage = (key: string, value: string) => {
	localStorage.setItem(key, value);
};

export const getLocalStorage = (key: string) => {
	const storedAuthData = localStorage.getItem(key);

	if (!storedAuthData) return null;

	try {
		return JSON.parse(storedAuthData);
	} catch {
		return null;
	}
};

export const deleteLocalStorage = (key: string) => {
	localStorage.removeItem(key);
};
