async function fetchTickets(email) {
  try {
    const url = 'http://127.0.0.1:8000/'; // Your GraphQL endpoint URL

    const response = await fetch(url, {
      method: 'POST',
      credentials: 'include',
      headers: {
        'Content-Type': 'application/json',
        'Access-Control-Allow-Origin': '*',
      },
      body: JSON.stringify({
        query: `
            query ($email: String) {
              users(email: $email) {
                id
                email
                tickets {
                  id
                  holderName
                  type
                  orderId
                  price
                  event {
                    id
                    eventName
                    country
                    location
                    startDate
                    endDate
                  }
                }
              }
            }
          `,
        variables: {
          email: email,
        },
        operationName: null,
        schema: 'schema',
      }),
    });
    const data = await response.json();
    return data.data.users['0'].tickets;

  } catch (error) {
    console.error(error);
    return [];

  }
}

export default fetchTickets;
