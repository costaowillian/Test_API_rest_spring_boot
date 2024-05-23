import React, { Suspense, lazy } from "react";
import { BrowserRouter, Route, Routes} from "react-router-dom";


const Login = lazy(() => import('./pages/login'));
const Books = lazy(() => import('./pages/books'));
const NewBook = lazy(() => import('./pages/newBook'));

export default function Router() {
    return (
        <BrowserRouter>
            <Suspense fallback={<div>Loading...</div>}>
                <Routes>
                    <Route path="/" element={<Login />} />
                    <Route path="/books" element={<Books />} />
                    <Route path="/book/new" element={<NewBook />} />
                </Routes>
            </Suspense>
        </BrowserRouter>
    );
}