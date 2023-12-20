async function getTicketById(id) {
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
          query($id: ID!) {
            ticket(id: $id) {
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
        `,
        variables: {
          id: id,
        },
        operationName: null,
        schema: 'schema',
      }),
    });
    const data = await response.json();
    console.log(data);
    return data.data.ticket;

  } catch (error) {
    console.error(error);
    return [];

  }
}

export default getTicketById;