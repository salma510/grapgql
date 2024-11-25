package ma.ensa.TP_graph.network

import com.apollographql.apollo3.ApolloClient


object ApolloClientProvider {
    fun getApolloClient(): ApolloClient {
        return ApolloClient.Builder()
            .serverUrl("http://10.0.2.2:8000/graphql")
            .build()
    }
}
