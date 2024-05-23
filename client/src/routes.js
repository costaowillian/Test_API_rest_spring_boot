import React, { Suspense, lazy } from "react";
import { BrowserRouter, Route, Routes} from "react-router-dom";


const Login = lazy(() => import('./pages/login'));

export default function Router() {
    return (
        <BrowserRouter>
            <Suspense fallback={<div>Loading...</div>}>
                <Routes>
                    <Route path="/" element={<Login />} />
                </Routes>
            </Suspense>
        </BrowserRouter>
    );
}