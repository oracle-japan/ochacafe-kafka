import http from 'k6/http';

const baseEndpoint = __ENV.BASE_URL || 'http://localhost:8080'

// List of recipes and weights
const events = [
    {
        event: {
            title: 'Fast learning Go',
            speaker: 'Takuya Niita',
            date: '2024-02-07 19:00'
        },
        weight: 2
    },
    {
        event: {
            title: 'Next.js basics',
            speaker: 'Kyotaro Nonaka',
            date: '2024-03-06 19:00'
        },
        weight: 2
    },
    {
        event: {
            title: 'Shin Kafka',
            speaker: 'Shuhei Kawamura',
            date: '2024-04-10 19:00'
        },
        weight: 2
    },
    {
        event: {
            title: 'Landscape of NewSQL',
            speaker: 'Yutaka Ichikawa',
            date: '2024-05-08 19:00'
        },
        weight: 50
    },
    {
        event: {
            title: 'IaaS infrastructure build with Kubernetes',
            speaker: 'Takuya Niita',
            date: '2024-06-05 19:00'
        },
        weight: 10
    },
    {
        event: {
            title: 'Ecosystem of LLM',
            speaker: 'Shuhei Kawamura',
            date: '2024-07-10 19:00'
        },
        weight: 10
    },
]

export const options = {
    stages: [
        { duration: '3m', target: 10 },
        { duration: '10m', target: 20 },
        { duration: '1m', target: 0 },
    ],
    noConnectionReuse: true,
};

export default function () {
    const headers = { 'Content-Type': 'application/json' };
    const event = getRandomItemWeighted(events);
    console.log(event)
    http.post(`${baseEndpoint}/kafka/produce`, JSON.stringify(event), { headers: headers });
}

function getRandomItemWeighted(items) {
    const totalWeight = items.reduce((acc, item) => acc + item.weight, 0);
    const randomWeight = Math.random() * totalWeight;
    let cumulativeWeight = 0;
    for (const item of items) {
        cumulativeWeight += item.weight;
        if (randomWeight <= cumulativeWeight) {
            return item.event;
        }
    }
}
